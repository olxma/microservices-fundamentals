package com.epam.microservices.songservice.mapper;

import com.epam.microservices.songservice.model.SongMetadata;
import com.epam.microservices.songservice.persistence.entry.Song;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface SongMapper {

    SongMapper INSTANCE = Mappers.getMapper(SongMapper.class);

    @Mapping(target = "songName", source = "name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Song toSong(SongMetadata metadata);

    @Mapping(target = "name", source = "songName")
    SongMetadata toSongMetadata(Song song);

    List<Integer> toListOfIds(Iterable<Song> songs);

    default Integer toInt(Song song) {
        return song.getId();
    }
}
