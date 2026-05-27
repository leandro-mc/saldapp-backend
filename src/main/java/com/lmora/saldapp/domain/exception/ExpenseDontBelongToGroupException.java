package com.lmora.saldapp.domain.exception;

import org.springframework.http.HttpStatus;

public class ExpenseDontBelongToGroupException extends SaldAppException{
    public ExpenseDontBelongToGroupException(Long id, Long groupId) {
        super(HttpStatus.CONFLICT.value(), String.format("Expense with id %d does not belong to group with id %d.", id, groupId));
    }
}