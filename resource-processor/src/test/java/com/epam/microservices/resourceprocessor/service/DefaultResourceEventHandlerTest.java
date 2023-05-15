package com.epam.microservices.resourceprocessor.service;

import com.epam.microservices.resourceprocessor.feign.ResourceServiceClient;
import com.epam.microservices.resourceprocessor.feign.SongServiceClient;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;

import java.time.Instant;

import static com.epam.microservices.resourceprocessor.TestUtil.getDefaultAudioFileMetadata;

@ExtendWith(MockitoExtension.class)
class DefaultResourceEventHandlerTest {

    @Mock
    private MetadataService metadataService;
    @Mock
    private ResourceServiceClient resourceServiceClient;
    @Mock
    private SongServiceClient songServiceClient;

    private DefaultResourceEventHandler consumer;

    @BeforeEach
    public void initConsumer() {
        consumer = new DefaultResourceEventHandler(metadataService, resourceServiceClient, songServiceClient);
    }

    @Test
    void consume_shouldInvokeSongService_whenGetEvent() {
        //given
        Integer resourceId = 1;
        ByteArrayResource resource = new ByteArrayResource(new byte[]{1}, "test");
        AudioFileMetadata metadata = getDefaultAudioFileMetadata(resourceId);
        ResourceEvent event = new ResourceEvent(resourceId, Instant.now());

        Mockito.when(resourceServiceClient.getResourceById(resourceId)).thenReturn(resource);
        Mockito.when(metadataService.extractMetadata(resource)).thenReturn(metadata);

        //when
        consumer.handle(event);

        //then
        Mockito.verify(songServiceClient, Mockito.times(1)).createSongMetadata(metadata);
    }
}
