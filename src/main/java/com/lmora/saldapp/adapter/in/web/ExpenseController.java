package com.lmora.saldapp.adapter.in.web;

import com.lmora.saldapp.adapter.in.web.dto.expese.CreateExpenseRequest;
import com.lmora.saldapp.adapter.in.web.dto.expese.ExpenseResponse;
import com.lmora.saldapp.adapter.in.web.dto.expese.UpdateExpenseRequest;
import com.lmora.saldapp.adapter.in.web.mapper.ExpenseWebMapper;
import com.lmora.saldapp.application.port.in.ExpenseUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups/{groupId}/expenses")
public class ExpenseController {

    private final ExpenseUseCase useCase;
    private final ExpenseWebMapper mapper;

    @PostMapping
    ResponseEntity<ExpenseResponse> createExpense(
            @Valid @RequestBody CreateExpenseRequest request,
            @PathVariable Long groupId)
    {
        ExpenseResponse response = mapper.toDto(useCase.createAndAssign(groupId, mapper.toDomain(request)));
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    ResponseEntity<ExpenseResponse> updateExpense(
            @Valid @RequestBody UpdateExpenseRequest request,
            @PathVariable Long groupId,
            @PathVariable Long id
    ){
        ExpenseResponse response = mapper.toDto(useCase.update(groupId, id, mapper.toDomain(request)));
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("{id}")
    ResponseEntity<ExpenseResponse> deleteExpense(
            @PathVariable Long groupId,
            @PathVariable Long id
    ){
        useCase.delete(groupId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{integrantId}")
    ResponseEntity<List<ExpenseResponse>> findAllExpensesByIntegrantId(
            @PathVariable Long groupId,
            @PathVariable Long integrantId
    ){
        List<ExpenseResponse> responses = useCase.findAllByIntegrant(groupId, integrantId).stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}
