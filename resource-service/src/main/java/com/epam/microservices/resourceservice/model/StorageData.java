package com.epam.microservices.resourceservice.model;

public record StorageData(Integer id, String storageType, String bucket, String path) {
}
