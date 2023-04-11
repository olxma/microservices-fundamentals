package com.epam.microservices.resourceservice.service;

import com.epam.microservices.resourceservice.exception.ResourceNotFoundException;
import com.epam.microservices.resourceservice.mapper.ResourceMapper;
import com.epam.microservices.resourceservice.model.ResourceData;
import com.epam.microservices.resourceservice.model.ResourceEvent;
import com.epam.microservices.resourceservice.persistence.entity.Resource;
import com.epam.microservices.resourceservice.persistence.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceService {

    private static final ResourceMapper MAPPER = ResourceMapper.INSTANCE;

    private final StorageService storageService;
    private final ResourceRepository repository;
    private final ResourceEventProducer<ResourceEvent> eventProducer;

    public Integer createResource(MultipartFile data) {
        String location = storageService.putAsStaging(data);
        Resource resource = new Resource(data.getOriginalFilename(), location);
        repository.save(resource);
        log.info("Resource {} was saved with status {}", location, resource.getState());
        eventProducer.sendMessage(new ResourceEvent(resource.getId(), resource.getCreatedAt()));
        return resource.getId();
    }

    public ResourceData getResourceById(Integer id) {
        ResourceData resourceData = repository.findById(id)
                .map(MAPPER::toResourceData)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        byte[] content = storageService.get(resourceData.getLocation(), resourceData.getState());
        resourceData.setContent(content);

        return resourceData;
    }

    public List<Integer> delete(List<Integer> ids) {
        Iterable<Resource> resources = repository.findAllById(ids);
        String[] locations = MAPPER.toListOfLocations(resources);
        storageService.deleteAll(locations);
        repository.deleteAll(resources);
        return MAPPER.toListOfIds(resources);
    }

    @Transactional
    public void updateAsProcessed(Integer id) {
        Resource resource = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
        resource.setState(Resource.State.PERMANENT);
        storageService.moveToPermanent(resource.getLocation());
        repository.save(resource);
        log.info("Resource {} was updated with status {}", resource.getLocation(), resource.getState());
    }
}
