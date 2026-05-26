package com.lmora.saldapp.adapter.in.web.exception;

import com.lmora.saldapp.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 - Resource Not Found
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFoundException(
        ResourceNotFoundException ex,
        HttpServletRequest request
    ){

        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            ex.getHttpStatusCode(),
            "Resource Not Found",
            ex.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
        return ResponseEntity.status(ex.getHttpStatusCode()).body(response);
    }

    // 409 - Conflict
    @ExceptionHandler({
            GroupClosedException.class,
            IntegrantHasExpensesException.class,
            IntegrantDontBelongToGroupException.class
    })
    public ResponseEntity<ErrorResponse>  handleConflictException(
        SaldAppException ex,
        HttpServletRequest request
    ){

        ErrorResponse response = new ErrorResponse(
            LocalDateTime.now(),
            ex.getHttpStatusCode(),
            "Conflict",
            ex.getMessage(),
            request.getRequestURI(),
            Map.of()
        );
        return ResponseEntity.status(ex.getHttpStatusCode()).body(response);
    }

     // 400 - validation errors with @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex,
        HttpServletRequest request
    ){
        Map<String, String> fieldErrors = ex.getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        error -> error.getDefaultMessage() != null
                                ? error.getDefaultMessage()
                                : "Invalid value"
                ));

        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                ex.getStatusCode().value(),
                "Bad Request",
                "Validation failed for one or more fields.",
                request.getRequestURI(),
                fieldErrors
        );
        return ResponseEntity.status(ex.getStatusCode()).body(response);
    }

    // 500 - unexpected errors
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(
//        Exception ex,
        HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                500,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later.",
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(500).body(response);
    }
}
