package com.epam.microservices.resourceprocessor.feign;

import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(value = "RESOURCE")
@LoadBalancerClient
public interface ResourceServiceClient {

    @GetMapping("/resources/{id}")
    ByteArrayResource getResourceById(@PathVariable Integer id);

    @PostMapping("/resources/{id}/processed")
    void processed(@PathVariable Integer id);
}
