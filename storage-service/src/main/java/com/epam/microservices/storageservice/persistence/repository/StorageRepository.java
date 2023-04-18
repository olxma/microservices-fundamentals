package com.epam.microservices.storageservice.persistence.repository;

import com.epam.microservices.storageservice.persistence.entity.Storage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StorageRepository extends JpaRepository<Storage, Integer> {

}
