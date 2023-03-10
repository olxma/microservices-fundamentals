package com.epam.microservices.resourseservice.persistence.repository;

import com.epam.microservices.resourseservice.persistence.entry.Resource;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResourceRepository extends CrudRepository<Resource, Integer> {
}
