package com.epam.microservices.resourseservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = FileNameLengthValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface FileNameConstraintLength {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "Invalid audio data type";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
