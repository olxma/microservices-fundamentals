package com.epam.microservices.storageservice.config;

import com.epam.microservices.storageservice.model.StorageObject;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties("amazon.s3")
public class StorageProperties {
    private List<StorageObject> storages;
}
