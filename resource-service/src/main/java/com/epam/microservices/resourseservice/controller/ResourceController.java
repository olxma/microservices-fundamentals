package com.epam.microservices.resourseservice.controller;

import com.epam.microservices.resourseservice.model.CreationResult;
import com.epam.microservices.resourseservice.model.DeletionParam;
import com.epam.microservices.resourseservice.model.DeletionResult;
import com.epam.microservices.resourseservice.service.ResourceService;
import com.epam.microservices.resourseservice.validator.FileNameConstraintLength;
import com.epam.microservices.resourseservice.validator.MP3TypeConstraint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/resources")
public class ResourceController {

    private final ResourceService service;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CreationResult> createResource(@FileNameConstraintLength(max = 255)
                                                         @MP3TypeConstraint MultipartFile data) {
        Integer id = service.createResource(data);
        return ResponseEntity.ok(new CreationResult(id));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ByteArrayResource> getResourceById(@PathVariable Integer id) {
        byte[] data = service.getResourceById(id);
        return ResponseEntity.ok().body(new ByteArrayResource(data));
    }

    @DeleteMapping
    public ResponseEntity<DeletionResult> deleteResources(@Valid @RequestParam("id") DeletionParam param) {
        List<Integer> deletedIds = service.delete(param.getIds());
        return ResponseEntity.ok().body(new DeletionResult(deletedIds));
    }
}
