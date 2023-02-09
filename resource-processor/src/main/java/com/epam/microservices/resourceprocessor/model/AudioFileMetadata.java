package com.epam.microservices.resourceprocessor.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.With;

@Getter
@Setter
@Builder
@ToString
public class AudioFileMetadata {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer year;
    @With
    private Integer resourceId;
}
