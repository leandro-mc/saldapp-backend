package com.lmora.saldapp.adapter.out.persistence.mapper;

import com.lmora.saldapp.adapter.out.persistence.entity.GroupEntity;
import com.lmora.saldapp.domain.model.Group;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface GroupEntityMapper {

    Group toDomain(GroupEntity groupEntity);
    GroupEntity toEntity(Group group);

}
