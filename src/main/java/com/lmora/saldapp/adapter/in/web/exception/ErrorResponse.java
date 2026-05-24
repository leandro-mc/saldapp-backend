package com.lmora.saldapp.adapter.in.web.exception;

import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<FieldError> errors // Only 400 error (bad request)
) {
}
