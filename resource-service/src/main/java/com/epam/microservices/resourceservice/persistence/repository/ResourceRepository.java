package com.epam.microservices.resourceservice.persistence.repository;

import com.epam.microservices.resourceservice.persistence.entity.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Integer> {
}
