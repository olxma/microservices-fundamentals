package com.epam.microservices.resourceservice.config;

import com.epam.microservices.resourceservice.model.ResourceEvent;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration(proxyBeanMethods = false)
@RequiredArgsConstructor
public class ManualConfiguration {
    private final KafkaTemplate<Long, ResourceEvent> kafkaTemplate;

    @PostConstruct
    void setup() {
        kafkaTemplate.setObservationEnabled(true);
    }
}
