package com.lmora.saldapp.domain.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends SaldAppException {
    public ResourceNotFoundException(String resourceName, Long resourceId) {
        super(HttpStatus.NOT_FOUND.value(), String.format("%s with id %d not found.", resourceName, resourceId));
    }
}
