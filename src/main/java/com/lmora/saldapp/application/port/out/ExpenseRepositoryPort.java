package com.lmora.saldapp.application.port.out;

import com.lmora.saldapp.domain.model.Expense;

import java.util.List;
import java.util.Optional;

public interface ExpenseRepositoryPort {
    Expense save(Expense expense);
    void deleteById(Long id);
    Optional<Expense> findById(Long id);
    List<Expense> findAllByIntegrant(Long integrantId);
    List<Expense> findAllByGroup(Long groupId);
}
