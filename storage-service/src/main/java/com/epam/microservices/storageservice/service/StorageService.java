package com.epam.microservices.storageservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.epam.microservices.storageservice.config.StorageProperties;
import com.epam.microservices.storageservice.mapper.StorageMapper;
import com.epam.microservices.storageservice.model.StorageData;
import com.epam.microservices.storageservice.model.StorageObject;
import com.epam.microservices.storageservice.persistence.entity.Storage;
import com.epam.microservices.storageservice.persistence.repository.StorageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

    private static final StorageMapper MAPPER = StorageMapper.INSTANCE;
    private final StorageRepository repository;
    private final AmazonS3 s3Client;
    private final StorageProperties properties;

    @Transactional
    public Integer createStorage(StorageObject newStorage) {
        Integer id;
        String bucketName = newStorage.bucket();
        boolean bucketExist = s3Client.doesBucketExistV2(bucketName);
        if (bucketExist) {
            log.info("Found Amazon S3 bucket with name '{}'", bucketName);
            id = repository.findAll()
                    .stream().filter(s -> s.getBucket().equals(bucketName))
                    .map(Storage::getId).findFirst()
                    .orElse(null);
        } else {
            log.info("Amazon S3 bucket with name '{}' was created successfully", bucketName);
            Storage storage = repository.save(MAPPER.toEntity(newStorage));
            id = storage.getId();
            s3Client.createBucket(bucketName);
        }
        return id;
    }

    public List<StorageData> getAllStorages() {
        return repository.findAll().stream().map(MAPPER::toDto).toList();
    }

    public List<Integer> delete(List<Integer> ids) {
        List<Storage> storages = repository.findAllById(ids);
        repository.deleteAll(storages);
        return storages.stream().map(Storage::getId).toList();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createS3Bucket() {
        properties.getStorages().forEach(this::createStorage);
    }
}
