package com.lmora.saldapp.adapter.in.web.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> errors // Only 400 error (bad request)
) {
}
