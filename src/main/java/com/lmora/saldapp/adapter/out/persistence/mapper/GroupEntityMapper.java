package com.lmora.saldapp.adapter.out.persistence.mapper;

import com.lmora.saldapp.adapter.out.persistence.entity.GroupEntity;
import com.lmora.saldapp.domain.model.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupEntityMapper {

    Group entityToDomain(GroupEntity groupEntity);
    GroupEntity domainToEntity(Group group);

}
