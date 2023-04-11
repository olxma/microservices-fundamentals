package com.epam.microservices.storageservice.model;

public record StorageObject(String storageType, String bucket, String path) {
}
