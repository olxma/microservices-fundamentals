package com.epam.microservices.resourceservice.service;

public interface ResourceEventProducer<T> {
    void sendMessage(T object);
}
