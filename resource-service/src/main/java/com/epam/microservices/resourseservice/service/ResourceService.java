package com.epam.microservices.resourseservice.service;

import com.epam.microservices.resourseservice.exception.ResourceNotFoundException;
import com.epam.microservices.resourseservice.mapper.ResourceMapper;
import com.epam.microservices.resourseservice.model.ResourceData;
import com.epam.microservices.resourseservice.model.ResourceEvent;
import com.epam.microservices.resourseservice.persistence.entry.Resource;
import com.epam.microservices.resourseservice.persistence.repository.ResourceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class ResourceService {

    private static final ResourceMapper MAPPER = ResourceMapper.INSTANCE;

    private final StorageService storageService;
    private final ResourceRepository repository;
    private final ResourceEventProducer<ResourceEvent> eventProducer;

    public Integer createResource(MultipartFile data) {
        String location = storageService.put(data);
        Resource resource = new Resource(data.getOriginalFilename(), location);
        repository.save(resource);
        eventProducer.sendMessage(new ResourceEvent(resource.getId(), resource.getCreatedAt()));
        return resource.getId();
    }

    public ResourceData getResourceById(Integer id) {
        ResourceData resourceData = repository.findById(id)
                .map(MAPPER::toResourceData)
                .orElseThrow(() -> new ResourceNotFoundException(id));
        byte[] content = storageService.get(resourceData.getLocation());
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
}
