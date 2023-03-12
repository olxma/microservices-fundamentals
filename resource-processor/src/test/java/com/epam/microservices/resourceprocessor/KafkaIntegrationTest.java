package com.epam.microservices.resourceprocessor;

import com.epam.microservices.resourceprocessor.feign.ResourceServiceClient;
import com.epam.microservices.resourceprocessor.feign.SongServiceClient;
import com.epam.microservices.resourceprocessor.model.AudioFileMetadata;
import com.epam.microservices.resourceprocessor.model.ResourceEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.time.Instant;

/* AN EXAMPLE OF INTEGRATION TESTS */

@SpringBootTest
@DirtiesContext
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
class KafkaIntegrationTest {

    private static final String TEST_FILE = "test.mp3";
    private static final String TEST_METADATA = "expected_metadata_integration.json";
    private static final long TIMEOUT_MILLIS = 1000L;

    @Value("${spring.kafka.consumer.resource-event-topic}")
    private String topic;

    @MockBean
    private ResourceServiceClient resourceServiceClient;
    @MockBean
    private SongServiceClient songServiceClient;
    @Autowired
    KafkaTemplate<Integer, ResourceEvent> kafkaTemplate;

    @Test
    void testKafkaIntegration() throws IOException {
        //given
        Integer id = 1;
        ByteArrayResource resource = TestUtil.getFileAsByteArrayResource(TEST_FILE);
        AudioFileMetadata metadata = TestUtil.readValueFromFile(TEST_METADATA, AudioFileMetadata.class);
        Mockito.when(resourceServiceClient.getResourceById(id)).thenReturn(resource);

        //when
        kafkaTemplate.send(topic, id, new ResourceEvent(id, Instant.now()));

        //then
        Mockito.verify(songServiceClient, Mockito.timeout(TIMEOUT_MILLIS).times(1))
                .createSongMetadata(metadata);
    }
}
