package com.epam.microservices.resourseservice.service;

import com.epam.microservices.resourseservice.exception.ResourceNotFoundException;
import com.epam.microservices.resourseservice.mapper.ResourceMapper;
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

    public Integer createResource(MultipartFile data) {
        String location = storageService.put(data);
        Resource resource = new Resource(data.getOriginalFilename(), location);
        return repository.save(resource).getId();
    }

    public byte[] getResourceById(Integer id) {
        return repository.findById(id)
                .map(Resource::getLocation)
                .map(storageService::get)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Integer> delete(List<Integer> ids) {
        Iterable<Resource> resources = repository.findAllById(ids);
        String[] locations = MAPPER.toListOfLocations(resources);
        storageService.deleteAll(locations);
        repository.deleteAll(resources);
        return MAPPER.toListOfIds(resources);
    }
}
