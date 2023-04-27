package com.epam.microservices.resourceprocessor.config;

import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import com.epam.microservices.resourceprocessor.service.ResourceEventHandler;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;

@Slf4j
@RequiredArgsConstructor
public class MyKafkaListener {

    private final ResourceEventHandler<ResourceEvent> consumer;
    private final ObservationRegistry observationRegistry;

    @KafkaListener(
            topics = "${spring.kafka.consumer.resource-event-topic}",
            groupId = "${spring.kafka.consumer.group-id}")
    void onMessage(ResourceEvent event) {
        Observation.tryScoped(observationRegistry.getCurrentObservation(), () -> {
            log.info("Got a message <{}>", event);
            consumer.handle(event);
        });
    }
}
