package com.epam.microservices.resourceprocessor.service;

public interface ResourceEventHandler<T> {
    void handle(T event);
}
