package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantRequest;
import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantResponse;
import com.lmora.saldapp.domain.model.Integrant;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IntegrantWebMapper {

    Integrant toDomain(IntegrantRequest request);
    IntegrantResponse toDto(Integrant integrant);
}
