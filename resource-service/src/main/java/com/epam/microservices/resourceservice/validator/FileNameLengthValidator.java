package com.epam.microservices.resourceservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

public class FileNameLengthValidator implements ConstraintValidator<FileNameConstraintLength, MultipartFile> {

    private int min;
    private int max;

    @Override
    public void initialize(FileNameConstraintLength constraint) {
        this.min = constraint.min();
        this.max = constraint.max();
    }

    @Override
    public boolean isValid(MultipartFile data, ConstraintValidatorContext context) {
        String fileName = data.getOriginalFilename();
        return fileName != null && fileName.length() >= min && fileName.length() <= max;
    }
}
