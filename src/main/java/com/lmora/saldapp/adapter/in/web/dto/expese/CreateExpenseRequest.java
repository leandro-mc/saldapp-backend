package com.lmora.saldapp.adapter.in.web.dto.expese;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateExpenseRequest(
        @NotNull(message = "Payed by is required")
        Long payedBy,

        @NotBlank(message = "Description is required")
        @Size(min = 4, max = 500, message = "Description must be between 4 and 500 characters")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount,

        @NotNull(message = "Date is required")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")      // ISO 8601 format
        LocalDateTime date
) {
}
