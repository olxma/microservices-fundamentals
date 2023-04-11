package com.epam.microservices.resourceservice.feign;

import com.epam.microservices.resourceservice.model.StorageData;
import com.epam.microservices.resourceservice.model.StorageObject;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(value = "STORAGE")
@LoadBalancerClient
public interface StorageServiceClient {

    @PostMapping("/storages")
    Integer createStorage(@RequestBody StorageObject newStorage);

    @GetMapping("/storages")
    @CircuitBreaker(name = "StorageService", fallbackMethod = "defaultStorageData")
    List<StorageData> getAllStorages();

    default List<StorageData> defaultStorageData(Exception ex) {
        return List.of(
                new StorageData(null, "STAGING", "staging", "/temp"),
                new StorageData(null, "PERMANENT", "permanent", "/files"));
    }
}
