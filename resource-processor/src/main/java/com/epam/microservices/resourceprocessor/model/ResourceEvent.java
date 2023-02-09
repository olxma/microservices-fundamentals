package com.epam.microservices.resourceprocessor.model;

import java.time.Instant;

public record ResourceEvent(Integer resourceId, Instant createdAt) {
}
