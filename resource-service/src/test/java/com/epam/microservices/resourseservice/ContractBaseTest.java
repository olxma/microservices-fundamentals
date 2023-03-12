package com.epam.microservices.resourseservice;

import com.epam.microservices.resourseservice.controller.ResourceController;
import com.epam.microservices.resourseservice.model.ResourceData;
import com.epam.microservices.resourseservice.service.ResourceService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.test.web.servlet.setup.StandaloneMockMvcBuilder;

import java.io.IOException;

import static com.epam.microservices.resourseservice.TestUtil.getFileAsByteArray;

@DirtiesContext
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public abstract class ContractBaseTest {

    @Autowired
    private ResourceController resourceController;

    @MockBean
    private ResourceService service;

    @BeforeEach
    public void setup() throws IOException {
        StandaloneMockMvcBuilder standaloneMockMvcBuilder
                = MockMvcBuilders.standaloneSetup(resourceController);
        RestAssuredMockMvc.standaloneSetup(standaloneMockMvcBuilder);

        ResourceData data = new ResourceData("test", "test.mp3", getFileAsByteArray("contracts/test.mp3"));
        Mockito.when(service.getResourceById(1)).thenReturn(data);
    }
}
