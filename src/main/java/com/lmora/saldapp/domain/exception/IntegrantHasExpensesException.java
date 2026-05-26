package com.lmora.saldapp.domain.exception;

import org.springframework.http.HttpStatus;

public class IntegrantHasExpensesException extends SaldAppException{
    public IntegrantHasExpensesException(Long resourceId) {
        super(HttpStatus.CONFLICT.value(), String.format("Integrant with id %d has expenses and cannot be deleted.", resourceId));
    }
}
