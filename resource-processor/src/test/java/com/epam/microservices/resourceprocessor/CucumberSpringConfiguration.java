package com.epam.microservices.resourceprocessor;

import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

@DirtiesContext
@SpringBootTest
@ActiveProfiles("test")
@CucumberContextConfiguration
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class CucumberSpringConfiguration {

    private static final String TEST_FILE = "test.mp3";
    private static final String TEST_METADATA = "expected_metadata_integration.json";

    @Value("${spring.kafka.consumer.resource-event-topic}")
    protected String topic;

    @Autowired
    protected KafkaTemplate<Integer, ResourceEvent> kafkaTemplate;

    protected static ByteArrayResource resource() throws IOException {
        return TestUtil.getFileAsByteArrayResource(TEST_FILE);
    }

    protected static String expectedMetadata() throws IOException {
        return TestUtil.readFileAsString(TEST_METADATA);
    }
}
