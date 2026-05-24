package com.lmora.saldapp.domain.exception;

public class ResourceNotFoundException extends SaldAppException {
    public ResourceNotFoundException(String resourceName, int resourceId) {
        super(String.format("%s with id %d not found.", resourceName, resourceId));
    }
}
