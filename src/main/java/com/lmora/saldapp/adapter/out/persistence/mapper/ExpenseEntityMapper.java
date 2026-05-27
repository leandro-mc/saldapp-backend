package com.lmora.saldapp.adapter.out.persistence.mapper;

import com.lmora.saldapp.adapter.out.persistence.entity.ExpenseEntity;
import com.lmora.saldapp.domain.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseEntityMapper {

    @Mapping(target = "group", ignore = true)       // Add into Repository Adapter
    @Mapping(target = "payedBy", ignore = true)     // Add into Repository Adapter
    ExpenseEntity toEntity(Expense expense);

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "payedBy", source = "payedBy.id")
    Expense toDomain(ExpenseEntity expenseEntity);
}
