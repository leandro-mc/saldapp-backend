package com.lmora.saldapp.domain.exception;

import org.springframework.http.HttpStatus;

public class GroupClosedException extends SaldAppException {
    public GroupClosedException(Long groupID) {
        super(HttpStatus.CONFLICT.value(), String.format("Group with id %d is closed and cannot accept new transactions.", groupID));
    }
}
