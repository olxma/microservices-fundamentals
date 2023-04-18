package com.epam.microservices.storageservice.mapper;

import com.epam.microservices.storageservice.model.StorageData;
import com.epam.microservices.storageservice.model.StorageObject;
import com.epam.microservices.storageservice.persistence.entity.Storage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface StorageMapper {
    StorageMapper INSTANCE = Mappers.getMapper(StorageMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Storage toEntity(StorageObject storageObject);

    StorageData toDto(Storage storage);
}
