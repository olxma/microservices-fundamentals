package com.epam.microservices.resourceprocessor.config;

import com.epam.microservices.resourceprocessor.exception.ExtractMetadataException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

@Slf4j
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.consumer.backoff-interval}")
    private long interval;

    @Value("${spring.kafka.consumer.max-attempts}")
    private int maxAttempts;

    @Bean
    public DefaultErrorHandler errorHandler() {
        BackOff fixedBackOff = new FixedBackOff(interval, maxAttempts);
        DefaultErrorHandler errorHandler = new DefaultErrorHandler((consumerRecord, e) ->
                log.error("Couldn't not consume and process a message: {}", consumerRecord), fixedBackOff);
        errorHandler.addNotRetryableExceptions(ExtractMetadataException.class);
        return errorHandler;
    }

}
