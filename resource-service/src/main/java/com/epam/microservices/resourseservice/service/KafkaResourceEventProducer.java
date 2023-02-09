package com.epam.microservices.resourseservice.service;

import com.epam.microservices.resourseservice.model.ResourceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaResourceEventProducer implements ResourceEventProducer<ResourceEvent> {

    @Value("${resource-event-topic}")
    private String topic;

    private final KafkaTemplate<Long, ResourceEvent> kafkaTemplate;

    @Override
    public void sendMessage(ResourceEvent event) {
        kafkaTemplate.send(topic, event);
    }
}
