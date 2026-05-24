package com.lmora.saldapp.domain.exception;

public abstract class SaldAppException extends RuntimeException {
    public SaldAppException(String message) {
        super(message);
    }
}
