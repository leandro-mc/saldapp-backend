package com.lmora.saldapp.adapter.in.web.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.lmora.saldapp.domain.exception.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDate;
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
            IntegrantDontBelongToGroupException.class,
            ExpenseDontBelongToGroupException.class,
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
                                : "Invalid value",
                        (first, second) -> first + ", " + second
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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleHttpMessageNotReadable(
//            HttpMessageNotReadableException ex,
            HttpServletRequest request
    ) {
        ErrorResponse response = new ErrorResponse(
                LocalDateTime.now(),
                400,
                "Bad Request",
                "Invalid request body: check field formats",
                request.getRequestURI(),
                Map.of()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
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
