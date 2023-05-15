package com.epam.microservices.songservice.service;

import com.epam.microservices.songservice.exception.SongNotFoundException;
import com.epam.microservices.songservice.mapper.SongMapper;
import com.epam.microservices.songservice.model.SongMetadata;
import com.epam.microservices.songservice.persistence.entity.Song;
import com.epam.microservices.songservice.persistence.repository.SongRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SongMetadataService {

    private static final SongMapper MAPPER = SongMapper.INSTANCE;

    private final SongRepository repository;

    public Integer create(SongMetadata metadata) {
        log.info("Saving a song: {}", metadata);
        return repository.save(MAPPER.toSong(metadata)).getId();
    }

    public SongMetadata getSongMetadataById(Integer id) {
        log.info("Getting song metadata by id: {}", id);
        return repository.findById(id)
                .map(MAPPER::toSongMetadata)
                .orElseThrow(() -> new SongNotFoundException(id));
    }

    public List<Integer> delete(List<Integer> ids) {
        Iterable<Song> songs = repository.findAllById(ids);
        repository.deleteAll(songs);
        return MAPPER.toListOfIds(songs);
    }
}
