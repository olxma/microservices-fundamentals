package com.epam.microservices;

import com.epam.microservices.model.AudioFileMetadata;
import com.epam.microservices.service.MP3MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;

@Slf4j
@SpringBootApplication
public class ResourceProcessorApplication implements CommandLineRunner {
    @Autowired
    MP3MetadataService service;

    public static void main(String[] args) {
        SpringApplication.run(ResourceProcessorApplication.class, args);
    }

    @Override
    public void run(String... args) {
        String path = args[0];
        System.out.println("Parsing file: " + path);

        AudioFileMetadata metadata = service.extractMetadata(new File(path));
        System.out.println(metadata);
    }
}
