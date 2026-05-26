package com.lmora.saldapp.domain.exception;

import org.springframework.http.HttpStatus;

public class IntegrantDontBelongToGroupException extends SaldAppException{
    public IntegrantDontBelongToGroupException(Long id, Long groupId) {
        super(HttpStatus.CONFLICT.value(), String.format("Integrant with id %d does not belong to group with id %d.", id, groupId));
    }
}
