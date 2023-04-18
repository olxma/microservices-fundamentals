package com.epam.microservices.songservice.service;

import com.epam.microservices.songservice.exception.SongNotFoundException;
import com.epam.microservices.songservice.model.SongMetadata;
import com.epam.microservices.songservice.persistence.entity.Song;
import com.epam.microservices.songservice.persistence.repository.SongRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.epam.microservices.songservice.TestUtil.getDefaultSong;
import static com.epam.microservices.songservice.TestUtil.getDefaultSongMetadata;
import static com.epam.microservices.songservice.TestUtil.getDefaultSongWithId;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class SongMetadataServiceTest {

    @Mock
    private SongRepository repository;

    private SongMetadataService service;

    @BeforeEach
    public void initService() {
        service = new SongMetadataService(repository);
    }

    @Test
    void create_shouldSaveAndReturnId_whenCreateMetadata() {
        //given
        Integer id = 10;
        Integer resourceId = 1;
        SongMetadata metadata = getDefaultSongMetadata(resourceId);

        Song song = getDefaultSong(resourceId);
        Mockito.when(repository.save(song)).thenReturn(song.withId(id));

        //when
        Integer result = service.create(metadata);

        //then
        assertEquals(id, result);
    }

    @Test
    void getSongMetadataById_shouldReturnSong_whenSongExists() {
        //given
        Integer id = 1;
        Integer resourceId = 10;
        Song song = getDefaultSongWithId(id, resourceId);
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(song));

        //when
        SongMetadata result = service.getSongMetadataById(id);

        //then
        assertEquals(getDefaultSongMetadata(resourceId), result);
    }

    @Test
    void getSongMetadataById_shouldThrowException_whenNoSuchSong() {
        //given
        Integer id = 1;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        SongNotFoundException exception = assertThrows(SongNotFoundException.class, () -> service.getSongMetadataById(id));
        assertEquals(String.format("Song with id %d doesn't exist", id), exception.getMessage());
    }

    private static Stream<Arguments> provideIdsForTestDelete() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 3), List.of(1, 2, 3), List.of(1, 2, 3)),
                Arguments.of(List.of(1, 2, 3), List.of(1, 2), List.of(1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdsForTestDelete")
    void delete_shouldReturnListOfDeletedIds_onlyForExistingSongs(List<Integer> ids,
                                                         List<Integer> existedSongIds,
                                                         List<Integer> expected) {
        //given
        List<Song> songs = existedSongIds.stream().map(id -> Song.builder().id(id).build()).toList();
        Mockito.when(repository.findAllById(ids)).thenReturn(songs);

        //when
        List<Integer> result = service.delete(ids);

        //then
        Mockito.verify(repository, Mockito.times(1)).deleteAll(songs);
        assertEquals(expected, result);
    }
}
