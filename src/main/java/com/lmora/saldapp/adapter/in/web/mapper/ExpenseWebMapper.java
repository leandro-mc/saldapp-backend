package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.expese.CreateExpenseRequest;
import com.lmora.saldapp.adapter.in.web.dto.expese.ExpenseResponse;
import com.lmora.saldapp.adapter.in.web.dto.expese.UpdateExpenseRequest;
import com.lmora.saldapp.domain.model.Expense;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ExpenseWebMapper {
    @Mapping(target = "id", ignore = true) // Ignore ID when creating a new expense
    @Mapping(target = "groupId", ignore = true) // Group ID will be set in the service layer
    @Mapping(target = "createdAt", ignore = true)
    Expense toDomain(CreateExpenseRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Expense toDomain(UpdateExpenseRequest request);

    ExpenseResponse toDto(Expense expense);
}
