package com.epam.microservices.resourseservice.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class S3StorageService implements StorageService {
    @Override
    public String store(MultipartFile data) {
        // TODO: 29.01.2023 Implementation
        return null;
    }

    @Override
    public byte[] load(String location) {
        // TODO: 29.01.2023 Implementation
        return null;
    }

    @Override
    public void delete(String location) {
        // TODO: 29.01.2023 implementation
    }
}
