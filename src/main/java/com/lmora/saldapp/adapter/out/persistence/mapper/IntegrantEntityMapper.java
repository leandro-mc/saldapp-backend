package com.lmora.saldapp.adapter.out.persistence.mapper;

import com.lmora.saldapp.adapter.out.persistence.entity.IntegrantEntity;
import com.lmora.saldapp.domain.model.Integrant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IntegrantEntityMapper {

    @Mapping(target = "group", ignore = true)           // Add into Repository Adapter
    IntegrantEntity toEntity(Integrant integrant);

    @Mapping(target = "groupId", source = "group.id")
    Integrant toDomain(IntegrantEntity integrantEntity);

}
