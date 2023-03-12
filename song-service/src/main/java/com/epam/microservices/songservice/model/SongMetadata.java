package com.epam.microservices.songservice.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@Validated
public class SongMetadata {
    @NotBlank
    @Length(max = 255)
    private String name;

    @Length(max = 255)
    private String artist;

    @Length(max = 255)
    private String album;

    @Length(max = 8)
    private String length;

    @NotNull
    @Min(1)
    private Integer resourceId;

    private Integer year;
}
