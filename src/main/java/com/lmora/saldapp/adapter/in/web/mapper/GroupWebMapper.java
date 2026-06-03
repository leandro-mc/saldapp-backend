package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.group.CreateGroupRequest;
import com.lmora.saldapp.adapter.in.web.dto.group.GroupResponse;
import com.lmora.saldapp.adapter.in.web.dto.group.UpdateGroupRequest;
import com.lmora.saldapp.application.usecase.group.model.GroupDetailsResult;
import com.lmora.saldapp.domain.model.Group;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface GroupWebMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "startDate", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Group toDomain(UpdateGroupRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endDate", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Group toDomain(CreateGroupRequest request);

    @Mapping(source = "group.id", target = "id")
    @Mapping(source = "group.name", target = "name")
    @Mapping(source = "group.description", target = "description")
    @Mapping(source = "group.currency", target = "currency")
    @Mapping(source = "group.startDate", target = "startDate")
    @Mapping(source = "group.endDate", target = "endDate")
    @Mapping(source = "integrantCount", target = "integrantCount")
    @Mapping(source = "totalExpenses", target = "totalExpenses")
    GroupResponse toDto(GroupDetailsResult group);
}