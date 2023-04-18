package com.epam.microservices.songservice;

import com.epam.microservices.songservice.model.SongMetadata;
import com.epam.microservices.songservice.persistence.entity.Song;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestUtil {

    public static SongMetadata getDefaultSongMetadata(Integer resourceId) {
        return SongMetadata.builder()
                .name("name")
                .album("album")
                .artist("artist")
                .length("length")
                .year(2023)
                .resourceId(resourceId)
                .build();
    }

    public static Song getDefaultSong(Integer resourceId) {
        return getDefaultSongWithId(null, resourceId);
    }

    public static Song getDefaultSongWithId(Integer id, Integer resourceId) {
        return Song.builder()
                .id(id)
                .songName("name")
                .album("album")
                .artist("artist")
                .length("length")
                .year(2023)
                .resourceId(resourceId)
                .build();
    }
}
