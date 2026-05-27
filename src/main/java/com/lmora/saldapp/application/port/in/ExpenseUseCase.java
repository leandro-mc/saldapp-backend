package com.lmora.saldapp.application.port.in;


import com.lmora.saldapp.domain.model.Expense;

import java.util.List;

public interface ExpenseUseCase {
    Expense createAndAssign(Long groupId, Expense expense);
    Expense update(Long groupId, Long expenseId, Expense expense);
    void delete(Long groupId, Long expenseId);
    List<Expense> findAllByIntegrant(Long groupId,Long integrantId);
}
