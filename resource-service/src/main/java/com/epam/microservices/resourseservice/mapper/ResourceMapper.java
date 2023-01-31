package com.epam.microservices.resourseservice.mapper;

import com.epam.microservices.resourseservice.model.ResourceData;
import com.epam.microservices.resourseservice.persistence.entry.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper
public interface ResourceMapper {

    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    List<Integer> toListOfIds(Iterable<Resource> resources);

    default String[] toListOfLocations(Iterable<Resource> resources) {
        return StreamSupport.stream(resources.spliterator(), false)
                .map(Resource::getLocation).toArray(String[]::new);
    }

    default Integer toInt(Resource resource) {
        return resource.getId();
    }

    @Mapping(target = "content", ignore = true)
    ResourceData toResourceData(Resource resource);
}
