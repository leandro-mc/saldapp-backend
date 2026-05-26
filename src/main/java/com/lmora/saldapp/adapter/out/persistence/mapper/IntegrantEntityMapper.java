package com.lmora.saldapp.adapter.out.persistence.mapper;

import com.lmora.saldapp.adapter.out.persistence.entity.IntegrantEntity;
import com.lmora.saldapp.domain.model.Integrant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntegrantEntityMapper {
    IntegrantEntity toEntity(Integrant integrant);
    Integrant toDomain(IntegrantEntity integrantEntity);
}
