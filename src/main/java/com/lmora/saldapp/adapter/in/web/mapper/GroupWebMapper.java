package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.group.CreateGroupRequest;
import com.lmora.saldapp.adapter.in.web.dto.group.GroupResponse;
import com.lmora.saldapp.adapter.in.web.dto.group.UpdateGroupRequest;
import com.lmora.saldapp.domain.model.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupWebMapper {
    Group dtoToDomain(UpdateGroupRequest request);
    Group dtoToDomain(CreateGroupRequest request);

    GroupResponse domainToDto(Group group);
}