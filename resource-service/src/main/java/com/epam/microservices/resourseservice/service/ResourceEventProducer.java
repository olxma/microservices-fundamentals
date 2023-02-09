package com.epam.microservices.resourseservice.service;

public interface ResourceEventProducer<T> {
    void sendMessage(T object);
}
