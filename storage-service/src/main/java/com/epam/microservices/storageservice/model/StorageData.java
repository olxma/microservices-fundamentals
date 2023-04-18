package com.epam.microservices.storageservice.model;

public record StorageData(Integer id, String storageType, String bucket, String path) {
}
