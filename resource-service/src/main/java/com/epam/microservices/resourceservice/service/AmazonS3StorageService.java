package com.epam.microservices.resourceservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.epam.microservices.resourceservice.exception.ResourceStorageException;
import com.epam.microservices.resourceservice.feign.StorageServiceClient;
import com.epam.microservices.resourceservice.model.StorageData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

import static com.epam.microservices.resourceservice.persistence.entity.Resource.State.PERMANENT;
import static com.epam.microservices.resourceservice.persistence.entity.Resource.State.STAGING;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3StorageService implements StorageService {

    private static final String PATH_SEPARATOR = "/";
    private static final String PUT_ERROR_MESSAGE = "Could not put object to AWS S3 Storage";
    private static final String GET_ERROR_MESSAGE = "Could not get object from AWS S3 Storage";
    private static final String PUT_ERROR_MESSAGE_TEMPLATE = PUT_ERROR_MESSAGE + " [{}, {}, {}]";
    private static final String GET_ERROR_MESSAGE_TEMPLATE = GET_ERROR_MESSAGE + " [{}, {}]";

    private final AmazonS3 amazonS3;
    private final StorageServiceClient storageServiceClient;

    @Override
    public String putAsStaging(MultipartFile data) {
        StorageData storage = getStorageDataByState(STAGING.toString());
        String key = UUID.randomUUID().toString();
        String path = getPath(storage.path(), key);
        try {
            amazonS3.putObject(storage.bucket(), path, data.getInputStream(), metadata(data));
            return key;
        } catch (IOException e) {
            log.error(PUT_ERROR_MESSAGE_TEMPLATE, storage.bucket(), data.getOriginalFilename(), path, e);
            throw new ResourceStorageException(PUT_ERROR_MESSAGE, e);
        }
    }

    @Override
    public byte[] get(String location, String state) {
        StorageData storage = getStorageDataByState(state);
        String path = getPath(storage.path(), location);
        try {
            return amazonS3.getObject(storage.bucket(), path).getObjectContent().readAllBytes();
        } catch (IOException e) {
            log.error(GET_ERROR_MESSAGE_TEMPLATE, storage.bucket(), path, e);
            throw new ResourceStorageException(GET_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void deleteAll(String... locations) {
        StorageData storage = getStorageDataByState(PERMANENT.toString());
        for (int i = 0; i < locations.length; i++) {
            locations[i] = getPath(storage.path(), locations[i]);
        }
        amazonS3.deleteObjects(new DeleteObjectsRequest(storage.bucket()).withKeys(locations));
    }

    @Override
    public void moveToPermanent(String location) {
        StorageData stagingStorage = getStorageDataByState(STAGING.toString());
        StorageData permanentStorage = getStorageDataByState(PERMANENT.toString());
        String oldPath = getPath(stagingStorage.path(), location);
        String newPath = getPath(permanentStorage.path(), location);
        amazonS3.copyObject(stagingStorage.bucket(), oldPath, permanentStorage.bucket(), newPath);
        amazonS3.deleteObject(stagingStorage.bucket(), oldPath);
    }

    private StorageData getStorageDataByState(String state) {
        return storageServiceClient.getAllStorages().stream()
                .filter(storage -> storage.storageType().equals(state))
                .findFirst()
                .orElseThrow(() -> new ResourceStorageException("No storage for " + state + " state"));
    }

    private static ObjectMetadata metadata(MultipartFile data) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.getSize());
        metadata.setContentType(data.getContentType());
        return metadata;
    }

    private static String getPath(String path, String key) {
        return path + PATH_SEPARATOR + key;
    }
}
