package com.epam.microservices.resourseservice.mapper;

import com.epam.microservices.resourseservice.persistence.entry.Resource;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ResourceMapper {

    ResourceMapper INSTANCE = Mappers.getMapper(ResourceMapper.class);

    List<Integer> toListOfIds(Iterable<Resource> resources);

    default Integer toInt(Resource resource) {
        return resource.getId();
    }
}
