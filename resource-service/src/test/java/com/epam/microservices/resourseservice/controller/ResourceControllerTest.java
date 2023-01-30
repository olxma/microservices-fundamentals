package com.epam.microservices.resourseservice.controller;

import com.epam.microservices.resourseservice.exception.ResourceNotFoundException;
import com.epam.microservices.resourseservice.service.ResourceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ResourceControllerTest {

    private static final byte[] TEST_CONTENT = {1, 2, 3, 4};

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ResourceService service;

    @Test
    void givenMP3MultipartFile_whenPostWithRequestPart_thenReturnsOK() throws Exception {
        MockMultipartFile data = new MockMultipartFile("data", "song.mp3", "audio/mpeg", TEST_CONTENT);
        when(service.createResource(data)).thenReturn(1);

        mockMvc.perform(multipart("/resources").file(data))
                .andExpect(content().json("{\"id\":1}"))
                .andExpect(status().isOk());
    }

    @ParameterizedTest
    @CsvSource({
            "song.wav, audio/mpeg",
            "song.mp3, application/json",
            "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo" +
                    "ooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong, audio/mpeg"
    })
    void givenInvalidFileNameOrContentType_whenPostWithRequestPart_thenReturnsBadRequest(
            String name, String contentType) throws Exception {

        MockMultipartFile data = new MockMultipartFile("data", name, contentType, TEST_CONTENT);
        when(service.createResource(data)).thenReturn(1);

        mockMvc.perform(multipart("/resources").file(data))
                .andExpect(status().isBadRequest());
    }

    @Test
    void givenResourceIdAndRange_whenGetWithId_thenReturnsPartialContent() throws Exception {
        when(service.getResourceById(1)).thenReturn(new byte[]{1, 2, 3, 4});

        mockMvc.perform(get("/resources/1")
                        .header(HttpHeaders.RANGE, "bytes=0-1"))
                .andExpect(content().bytes(new byte[]{1, 2}))
                .andExpect(status().isPartialContent());
    }

    @Test
    void givenResourceId_whenGetWithId_thenReturnsOK() throws Exception {
        when(service.getResourceById(1)).thenReturn(new byte[]{1, 2, 3, 4});

        mockMvc.perform(get("/resources/1"))
                .andExpect(content().bytes(new byte[]{1, 2, 3, 4}))
                .andExpect(status().isOk());
    }

    @Test
    void givenNotExistingResourceId_whenGetWithId_thenReturnsNotFound() throws Exception {
        when(service.getResourceById(1)).thenThrow(ResourceNotFoundException.class);

        mockMvc.perform(get("/resources/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void givenResourceIds_whenDelete_thenReturnOK() throws Exception {
        List<Integer> ids = List.of(1, 2, 3);
        when(service.delete(ids)).thenReturn(ids);

        mockMvc.perform(delete("/resources").param("id", "1,2,3"))
                .andExpect(content().json("{\"ids\":[1,2,3]}"))
                .andExpect(status().isOk());
    }

    @Test
    void givenResourceIdsWithOneThatNotExist_whenDelete_thenReturnOK() throws Exception {
        List<Integer> ids = List.of(1, 2, 3);
        when(service.delete(List.of(1, 2, 3, 4))).thenReturn(ids);

        mockMvc.perform(delete("/resources").param("id", "1,2,3,4"))
                .andExpect(content().json("{\"ids\":[1,2,3]}"))
                .andExpect(status().isOk());
    }

    @Test
    void givenResourcesIdsMoreThan200Chars_whenDelete_thenReturnError() throws Exception {
        String tooManyIds = "1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33,34,35,36,37,38,39,40,41,42,43,44,45,46,47,48,49,50,51,52,53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72";

        mockMvc.perform(delete("/resources").param("id", tooManyIds))
                .andExpect(status().isBadRequest());
    }
}
