package com.epam.microservices.resourceprocessor;

import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class TestUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public static ByteArrayResource getFileAsByteArrayResource(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return new ByteArrayResource(Files.readAllBytes(file.toPath()));
    }

    public static <T> T readValueFromFile(String fileName, Class<T> tClass) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return OBJECT_MAPPER.readValue(file, tClass);
    }

    public static String readFileAsString(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return Files.readString(file.toPath());
    }

    public static AudioFileMetadata getDefaultAudioFileMetadata(Integer resourceId) {
        return AudioFileMetadata.builder()
                .name("name")
                .album("album")
                .artist("artist")
                .length("length")
                .year(2023)
                .resourceId(resourceId)
                .build();
    }
}
