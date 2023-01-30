package com.epam.microservices.resourseservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String put(MultipartFile data);

    byte[] get(String location);

    void delete(String location);
}
