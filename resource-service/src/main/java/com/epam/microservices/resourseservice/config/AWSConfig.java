package com.epam.microservices.resourseservice.config;

import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.HeadBucketRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

@Slf4j
@Configuration
public class AWSConfig {

    @Value("${amazon.s3.endpoint}")
    private String endpoint;

    @Value("${amazon.s3.region}")
    private String region;

    @Value("${amazon.s3.bucket-name}")
    private String bucketName;

    @Bean
    public AmazonS3 amazonS3() {
        return AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endpoint, region))
                .withCredentials(new DefaultAWSCredentialsProviderChain())
                .withPathStyleAccessEnabled(true)
                .build();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void createS3Bucket() {
        try {
            amazonS3().headBucket(new HeadBucketRequest(bucketName));
            log.info("Found Amazon S3 bucket with name '{}'", bucketName);
        } catch (AmazonS3Exception e) {
            log.info("Amazon S3 bucket with name '{}' was created successfully", bucketName);
            amazonS3().createBucket(bucketName);
        }
    }
}
