package com.epam.microservices.resourceprocessor.service;

import com.epam.microservices.resourceprocessor.feign.ResourceServiceClient;
import com.epam.microservices.resourceprocessor.feign.SongServiceClient;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class DefaultResourceEventHandler implements ResourceEventHandler<ResourceEvent> {

    private final MetadataService metadataService;
    private final ResourceServiceClient resourceServiceClient;
    private final SongServiceClient songServiceClient;

    @Override
    public void handle(ResourceEvent event) {
        Integer resourceId = event.resourceId();
        ByteArrayResource resource = resourceServiceClient.getResourceById(resourceId);
        AudioFileMetadata metadata = metadataService.extractMetadata(resource).withResourceId(resourceId);
        songServiceClient.createSongMetadata(metadata);
        resourceServiceClient.processed(resourceId);
    }
}
