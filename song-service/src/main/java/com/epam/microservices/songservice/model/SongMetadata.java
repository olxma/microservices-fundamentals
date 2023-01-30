package com.epam.microservices.songservice.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class SongMetadata {
    @NotBlank
    @Length(max = 255)
    private String name;

    @NotBlank
    @Length(max = 255)
    private String artist;

    @NotBlank
    @Length(max = 255)
    private String album;

    @NotBlank
    @Length(max = 8)
    private String length;

    @NotNull
    @Min(1)
    private Integer resourceId;

    @NotNull
    @Min(-5000)
    @Max(5000)
    private Integer year;
}
