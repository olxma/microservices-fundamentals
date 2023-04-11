package com.epam.microservices.storageservice.controller;

import com.epam.microservices.storageservice.model.CreationResult;
import com.epam.microservices.storageservice.model.DeletionParam;
import com.epam.microservices.storageservice.model.DeletionResult;
import com.epam.microservices.storageservice.model.StorageData;
import com.epam.microservices.storageservice.model.StorageObject;
import com.epam.microservices.storageservice.service.StorageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/storages")
public class StorageController {

    private final StorageService service;

    @PostMapping
    public ResponseEntity<CreationResult> createStorage(@RequestBody StorageObject newStorage) {
        Integer id = service.createStorage(newStorage);
        return ResponseEntity.ok(new CreationResult(id));
    }

    @GetMapping
    public ResponseEntity<List<StorageData>> getStorages() {
        List<StorageData> storageList = service.getAllStorages();
        return ResponseEntity.ok(storageList);
    }

    @DeleteMapping
    public ResponseEntity<DeletionResult> deleteStorages(@Valid @RequestParam("id") DeletionParam param) {
        List<Integer> deletedIds = service.delete(param.getIds());
        return ResponseEntity.ok().body(new DeletionResult(deletedIds));
    }
}
