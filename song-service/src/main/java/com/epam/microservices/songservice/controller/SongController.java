package com.epam.microservices.songservice.controller;

import com.epam.microservices.songservice.model.CreationResult;
import com.epam.microservices.songservice.model.DeletionParam;
import com.epam.microservices.songservice.model.DeletionResult;
import com.epam.microservices.songservice.model.SongMetadata;
import com.epam.microservices.songservice.service.SongMetadataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

    private final SongMetadataService service;

    @PostMapping
    public ResponseEntity<CreationResult> createSongMetadata(@Valid @RequestBody SongMetadata metadata) {
        Integer id = service.create(metadata);
        return ResponseEntity.ok(new CreationResult(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SongMetadata> getSongMetadataById(@PathVariable Integer id) {
        SongMetadata metadata = service.getSongMetadataById(id);
        return ResponseEntity.ok().body(metadata);
    }

    @DeleteMapping
    public ResponseEntity<DeletionResult> deleteResources(@Valid @RequestParam("id") DeletionParam param) {
        List<Integer> deletedIds = service.delete(param.getIds());
        return ResponseEntity.ok().body(new DeletionResult(deletedIds));
    }
}
