package com.epam.microservices.resourceprocessor.service;

import com.epam.microservices.resourceprocessor.exception.ExtractMetadataException;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;

import static com.epam.microservices.resourceprocessor.TestUtil.getFileAsByteArrayResource;
import static com.epam.microservices.resourceprocessor.TestUtil.readValueFromFile;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MetadataServiceTest {

    private static final String TEST_FILE = "test.mp3";
    private static final String EXPECTED_METADATA_FILE = "expected_metadata.json";
    private static final String BAD_TEST_FILE = "bad.txt";

    private MetadataService metadataService;

    @BeforeEach
    public void initMetadataService() {
        metadataService = new MetadataService();
    }

    @Test
    void extractMetadata_shouldReturnExpectedMetadata_whenValidResource() throws IOException {
        //given
        ByteArrayResource resource = getFileAsByteArrayResource(TEST_FILE);
        AudioFileMetadata expectedMetadata = readValueFromFile(EXPECTED_METADATA_FILE, AudioFileMetadata.class);

        //when
        AudioFileMetadata result = metadataService.extractMetadata(resource);

        //then
        assertNotNull(result);
        assertEquals(expectedMetadata, result);
    }

    @Test
    void extractMetadata_shouldThrowException_whenResourceIsNull() {
        //given
        //when
        //then
        assertThrows(ExtractMetadataException.class, () ->
                metadataService.extractMetadata(null));
    }

    @Test
    void extractMetadata_shouldThrowException_whenResourceIsEmpty() {
        //given
        ByteArrayResource resource = new ByteArrayResource(new byte[]{});

        //when
        //then
        assertThrows(ExtractMetadataException.class, () ->
                metadataService.extractMetadata(resource));
    }

    @Test
    void extractMetadata_shouldThrowException_whenResourceIsBad() throws IOException {
        //given
        ByteArrayResource resource = getFileAsByteArrayResource(BAD_TEST_FILE);

        //when
        //then
        assertThrows(ExtractMetadataException.class, () ->
                metadataService.extractMetadata(resource));
    }
}
