package com.epam.microservices.resourceservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String putAsStaging(MultipartFile data);

    byte[] get(String location, String state);

    void deleteAll(String... locations);

    void moveToPermanent(String location);
}
