package com.epam.microservices.resourceservice.service;

import com.epam.microservices.resourceservice.exception.ResourceNotFoundException;
import com.epam.microservices.resourceservice.model.ResourceData;
import com.epam.microservices.resourceservice.model.ResourceEvent;
import com.epam.microservices.resourceservice.persistence.entity.Resource;
import com.epam.microservices.resourceservice.persistence.repository.ResourceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.epam.microservices.resourceservice.persistence.entity.Resource.State.PERMANENT;
import static com.epam.microservices.resourceservice.persistence.entity.Resource.State.STAGING;
import static org.apache.http.entity.ContentType.MULTIPART_FORM_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class ResourceServiceTest {

    @Mock
    private StorageService storageService;
    @Mock
    private ResourceRepository repository;
    @Mock
    private ResourceEventProducer<ResourceEvent> eventProducer;

    private ResourceService resourceService;

    @BeforeEach
    public void initService() {
        resourceService = new ResourceService(storageService, repository, eventProducer);
    }

    @Test
    void createResource_shouldSendEventAndReturnId_whenCreateNewResource() {
        //given
        Integer id = 1;
        String location = "test-location";
        String originalName = "test-original-name";
        MultipartFile data = new MockMultipartFile("test", originalName,
                MULTIPART_FORM_DATA.getMimeType(), new byte[]{1});
        Resource resource = new Resource(data.getOriginalFilename(), location);
        Mockito.when(storageService.putAsStaging(data)).thenReturn(location);
        Mockito.when(repository.save(resource)).then(invocation -> {
            Resource r = invocation.getArgument(0, Resource.class);
            r.setId(id);
            return null;
        });

        //when
        Integer result = resourceService.createResource(data);

        //then
        Mockito.verify(eventProducer, Mockito.times(1))
                .sendMessage(new ResourceEvent(id, resource.getCreatedAt()));
        assertEquals(id, result);
    }

    @Test
    void getResourceById_shouldReturnExpectedData_whenResourceExists() {
        //given
        Integer id = 1;
        String location = "test-location";
        String fileName = "test-file-name";
        byte[] content = new byte[]{1};
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(new Resource(fileName, location)));
        Mockito.when(storageService.get(location, STAGING.toString())).thenReturn(content);

        //when
        ResourceData result = resourceService.getResourceById(id);

        //then
        ResourceData expected = new ResourceData(location, fileName, content, STAGING.toString());
        assertEquals(expected, result);
    }

    @Test
    void getResourceById_shouldThrowException_whenResourceNotExist() {
        //given
        Integer id = 1;
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());

        //when
        //then
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> resourceService.getResourceById(id));
        assertEquals(String.format("Resource with id %d doesn't exist", id), exception.getMessage());
    }

    private static Stream<Arguments> provideIdsForTestDelete() {
        return Stream.of(
                Arguments.of(List.of(1, 2, 3), List.of(1, 2, 3), List.of(1, 2, 3)),
                Arguments.of(List.of(1, 2, 3), List.of(1, 2), List.of(1, 2))
        );
    }

    @ParameterizedTest
    @MethodSource("provideIdsForTestDelete")
    void delete_shouldDeleteEverywhereAndReturnListOfDeletedIds_onlyForExistedResources(List<Integer> ids,
                                                                                        List<Integer> existedResourceIds,
                                                                                        List<Integer> expected) {
        //given
        List<Resource> resources = existedResourceIds.stream()
                .map(id -> new Resource(id, "file" + id, "location" + id, PERMANENT, Instant.now()))
                .toList();
        String[] locations = resources.stream().map(Resource::getLocation).toArray(String[]::new);
        Mockito.when(repository.findAllById(ids)).thenReturn(resources);

        //when
        List<Integer> result = resourceService.delete(ids);

        //then
        Mockito.verify(storageService, Mockito.times(1)).deleteAll(locations);
        storageService.deleteAll(locations);
        repository.deleteAll(resources);
        assertEquals(expected, result);
    }
}
