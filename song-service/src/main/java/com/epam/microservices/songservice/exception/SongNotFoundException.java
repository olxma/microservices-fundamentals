package com.epam.microservices.songservice.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class SongNotFoundException extends RuntimeException {

    private static final String MESSAGE_PATTERN = "Song with id %d doesn't exist";

    public SongNotFoundException(Integer id) {
        super(String.format(MESSAGE_PATTERN, id));
    }
}
