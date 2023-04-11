package com.epam.microservices.resourceservice.model;

public record StorageObject(String storageType, String bucket, String path) {
}
