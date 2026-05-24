package com.lmora.saldapp.domain.exception;

import lombok.Getter;

@Getter
public abstract class SaldAppException extends RuntimeException {
    private final int httpStatusCode;
    public SaldAppException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }
}
