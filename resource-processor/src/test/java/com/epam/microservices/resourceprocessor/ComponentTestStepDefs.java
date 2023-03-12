package com.epam.microservices.resourceprocessor;

import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.io.IOException;
import java.time.Instant;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalToJson;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.ok;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;

@WireMockTest
public class ComponentTestStepDefs extends CucumberSpringConfiguration {

    private WireMockServer resourceServiceServer;
    private WireMockServer songServiceServer;
    private ResourceEvent event;

    @Before
    public void setUp() {
        resourceServiceServer = new WireMockServer(8085);
        resourceServiceServer.start();

        songServiceServer = new WireMockServer(8086);
        songServiceServer.start();
    }

    @After
    public void shutdown() {
        resourceServiceServer.shutdown();
        songServiceServer.shutdown();
    }

    @Given("Resource creation event occurs on Resource Service side")
    public void resource_creation_event_occurs_on_Resource_Service_side() throws IOException {
        resourceServiceServer.stubFor(get("/resources/1").willReturn(aResponse().withBody(resource().getByteArray())));
        songServiceServer.stubFor(post("/songs").willReturn(ok()));
        event = new ResourceEvent(1, Instant.now());
    }

    @When("Resource Service sends event")
    public void Resource_Service_sends_event() {
        kafkaTemplate.send(topic, event);
    }

    @Then("Resource Processor handles income event and sends metadata to Song Service")
    public void Resource_Processor_handles_income_event_and_sends_metadata_to_Song_Service() {
        await().atMost(10, SECONDS).untilAsserted(() ->
                songServiceServer.verify(1, postRequestedFor(urlEqualTo("/songs"))
                        .withRequestBody(equalToJson(expectedMetadata(), true, true))));
    }
}
