package com.epam.microservices.resourseservice.model;

import java.time.Instant;

public record ResourceEvent(Integer resourceId, Instant createdAt) {
}
