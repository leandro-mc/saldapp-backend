package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantRequest;
import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantResponse;
import com.lmora.saldapp.domain.model.Integrant;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IntegrantWebMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    Integrant toDomain(IntegrantRequest request);

    IntegrantResponse toDto(Integrant integrant);
}
