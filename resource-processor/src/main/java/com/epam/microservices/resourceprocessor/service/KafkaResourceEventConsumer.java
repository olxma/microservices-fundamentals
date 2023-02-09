package com.epam.microservices.resourceprocessor.service;

import com.epam.microservices.resourceprocessor.feign.ResourceServiceClient;
import com.epam.microservices.resourceprocessor.feign.SongServiceClient;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaResourceEventConsumer implements ResourceEventConsumer<ResourceEvent> {

    private final MetadataService metadataService;
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;

    @KafkaListener(topics = "${resource-event-topic}", groupId = "${spring.kafka.consumer.group-id}")
    @Override
    public void consume(ResourceEvent event) {
        Integer resourceId = event.resourceId();
        ByteArrayResource resource = resourceServiceClient.getResourceById(resourceId);
        AudioFileMetadata metadata = metadataService.extractMetadata(resource).withResourceId(resourceId);
        songServiceClient.createSongMetadata(metadata);
    }
}
