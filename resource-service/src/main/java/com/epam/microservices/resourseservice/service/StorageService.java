package com.epam.microservices.resourseservice.service;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
    String store(MultipartFile data);

    byte[] load(String location);

    void delete(String location);
}
