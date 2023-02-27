package com.epam.microservices.resourseservice.persistence.entry;

import jakarta.persistence.Entity;
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
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String fileName;

    private String location;

    @CreationTimestamp
    private Instant createdAt;
}
