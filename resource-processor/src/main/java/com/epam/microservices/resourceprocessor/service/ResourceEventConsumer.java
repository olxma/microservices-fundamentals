package com.epam.microservices.resourceprocessor.service;

public interface ResourceEventConsumer<T> {
    void consume(T event);
}
