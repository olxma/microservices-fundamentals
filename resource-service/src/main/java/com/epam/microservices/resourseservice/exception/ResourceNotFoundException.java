package com.epam.microservices.resourseservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "Resource with id %d doesn't exist";

    public ResourceNotFoundException(Integer id) {
        super(String.format(MESSAGE_PATTERN, id));
    }
}
