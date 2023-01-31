package com.epam.microservices.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
public class AudioFileMetadata {
    private String songName;
    private String artist;
    private String album;
    private String length;
    private Integer year;
}
