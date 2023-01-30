package com.epam.microservices.resourseservice.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.epam.microservices.resourseservice.exception.ResourceStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3StorageService implements StorageService {

    private static final String PUT_ERROR_MESSAGE = "Could not put object to AWS S3 Storage";
    private static final String GET_ERROR_MESSAGE = "Could not get object from AWS S3 Storage";
    private static final String PUT_ERROR_MESSAGE_TEMPLATE = PUT_ERROR_MESSAGE + " [{}, {}, {}]";
    private static final String GET_ERROR_MESSAGE_TEMPLATE = GET_ERROR_MESSAGE + " [{}, {}]";

    private final AmazonS3 amazonS3;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Override
    public String put(MultipartFile data) {
        UUID key = UUID.randomUUID();
        try {
            amazonS3.putObject(bucketName, key.toString(), data.getInputStream(), metadata(data));
            return key.toString();
        } catch (IOException e) {
            log.error(PUT_ERROR_MESSAGE_TEMPLATE, bucketName, data.getOriginalFilename(), key, e);
            throw new ResourceStorageException(PUT_ERROR_MESSAGE, e);
        }
    }

    @Override
    public byte[] get(String location) {
        try {
            return amazonS3.getObject(bucketName, location).getObjectContent().readAllBytes();
        } catch (IOException e) {
            log.error(GET_ERROR_MESSAGE_TEMPLATE, "test", location, e);
            throw new ResourceStorageException(GET_ERROR_MESSAGE, e);
        }
    }

    @Override
    public void deleteAll(String... locations) {
        amazonS3.deleteObjects(new DeleteObjectsRequest(bucketName).withKeys(locations));
    }

    private static ObjectMetadata metadata(MultipartFile data) {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(data.getSize());
        metadata.setContentType(data.getContentType());
        return metadata;
    }
}
