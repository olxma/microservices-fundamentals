package com.epam.microservices.songservice.persistence.repository;

import com.epam.microservices.songservice.persistence.entry.Song;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends CrudRepository<Song, Integer> {
}
