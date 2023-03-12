package com.epam.microservices.resourceprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioFileMetadata {
    private String name;
    private String artist;
    private String album;
    private String length;
    private Integer year;
    @With
    private Integer resourceId;
}
