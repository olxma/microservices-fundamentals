package com.epam.microservices.resourceservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceStorageException extends RuntimeException {

    public ResourceStorageException(String message) {
        super(message);
    }
    public ResourceStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
