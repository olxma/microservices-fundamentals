package com.epam.microservices.resourseservice.errorhandling;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ResourceErrorHandlingAdvice {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> onConstraintViolationException(ConstraintViolationException ex) {
        return ResponseEntity.badRequest().body(ex.getMessage());
    }
}
