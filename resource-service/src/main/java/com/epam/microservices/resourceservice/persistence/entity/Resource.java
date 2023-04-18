package com.epam.microservices.resourceservice.persistence.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.With;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;

@Data
@With
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    public Resource(String fileName, String location) {
        this.location = location;
        this.fileName = fileName;
        this.state = State.STAGING;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String location;

    @Enumerated(EnumType.STRING)
    private State state;

    @CreationTimestamp
    private Instant createdAt;

    public enum State {
        STAGING, PERMANENT
    }
}
