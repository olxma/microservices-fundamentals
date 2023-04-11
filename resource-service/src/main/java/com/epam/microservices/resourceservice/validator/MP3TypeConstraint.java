package com.epam.microservices.resourceservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = MP3TypeValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface MP3TypeConstraint {
    String message() default "Invalid audio data type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
