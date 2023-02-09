package com.epam.microservices.resourceprocessor.mapper;

import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.epam.microservices.resourceprocessor.model.MetadataProperty;
import org.apache.tika.metadata.Metadata;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static java.util.Optional.ofNullable;

@Mapper
public interface AudioFileMetadataMapper {

    AudioFileMetadataMapper INSTANCE = Mappers.getMapper(AudioFileMetadataMapper.class);

    default AudioFileMetadata toAudioFileMetadata(Metadata metadata) {
        return AudioFileMetadata.builder()
                .name(metadata.get(MetadataProperty.TITLE.getTag()))
                .artist(metadata.get(MetadataProperty.ARTIST.getTag()))
                .album(metadata.get(MetadataProperty.ALBUM.getTag()))
                .length(toPrettyLength(metadata.get(MetadataProperty.DURATION.getTag())))
                .year(ofNullable(metadata.get(MetadataProperty.RELEASE_DATE.getTag()))
                        .map(Integer::parseInt).orElse(null))
                .build();
    }

    default String toPrettyLength(String durationInSeconds) {
        int seconds = Integer.parseInt(durationInSeconds.split("\\.")[0]);
        return String.format("%d:%02d", seconds / 60, seconds % 60);
    }
}
