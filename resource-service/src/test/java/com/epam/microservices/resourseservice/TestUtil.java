package com.epam.microservices.resourseservice;

import lombok.experimental.UtilityClass;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@UtilityClass
public class TestUtil {

    public static byte[] getFileAsByteArray(String fileName) throws IOException {
        File file = new ClassPathResource(fileName).getFile();
        return Files.readAllBytes(file.toPath());
    }
}
