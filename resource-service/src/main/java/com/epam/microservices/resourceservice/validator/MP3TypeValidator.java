package com.epam.microservices.resourceservice.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public class MP3TypeValidator implements ConstraintValidator<MP3TypeConstraint, MultipartFile> {

    private static final String AUDIO_MPEG = "audio/mpeg";
    private static final String MP3 = "mp3";

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        return isAudioMpeg(value) && isMP3(value);
    }

    private static boolean isAudioMpeg(MultipartFile value) {
        return Optional.ofNullable(value)
                .map(MultipartFile::getContentType)
                .filter(AUDIO_MPEG::equals)
                .isPresent();
    }

    private static boolean isMP3(MultipartFile value) {
        return Optional.ofNullable(value)
                .map(MultipartFile::getOriginalFilename)
                .map(MP3TypeValidator::getFileExtension)
                .filter(MP3::equals)
                .isPresent();
    }

    private static String getFileExtension(String filename) {
        return Optional.ofNullable(filename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1))
                .orElse(null);
    }
}
