package com.epam.microservices.resourseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ResourceStorageException extends RuntimeException {

    public ResourceStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
