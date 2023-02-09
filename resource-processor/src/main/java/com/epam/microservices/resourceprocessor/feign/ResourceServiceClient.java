package com.epam.microservices.resourceprocessor.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(value = "resourceService", url = "${feign.resource-service.host}")
public interface ResourceServiceClient {

    @GetMapping("/resources/{id}")
    ByteArrayResource getResourceById(@PathVariable Integer id);
}
