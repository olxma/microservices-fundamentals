package com.epam.microservices.resourceservice.model;

import java.time.Instant;

public record ResourceEvent(Integer resourceId, Instant createdAt) {
}
