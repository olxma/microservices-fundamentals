package com.epam.microservices.mapper;

import com.epam.microservices.model.AudioFileMetadata;
import com.epam.microservices.model.MetadataProperty;
import org.apache.tika.metadata.Metadata;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import static java.util.Optional.ofNullable;

@Mapper
public interface AudioFileMetadataMapper {

    AudioFileMetadataMapper INSTANCE = Mappers.getMapper(AudioFileMetadataMapper.class);

    default AudioFileMetadata toAudioFileMetadata(Metadata metadata) {
        return AudioFileMetadata.builder()
                .songName(metadata.get(MetadataProperty.TITLE.getTag()))
                .artist(metadata.get(MetadataProperty.ARTIST.getTag()))
                .album(metadata.get(MetadataProperty.ALBUM.getTag()))
                .length(metadata.get(MetadataProperty.DURATION.getTag()))
                .year(ofNullable(metadata.get(MetadataProperty.RELEASE_DATE.getTag()))
                        .map(Integer::parseInt).orElse(null))
                .build();
    }
}
