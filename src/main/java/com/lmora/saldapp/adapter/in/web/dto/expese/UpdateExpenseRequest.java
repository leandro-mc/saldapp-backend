package com.lmora.saldapp.adapter.in.web.dto.expese;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateExpenseRequest(
        @NotNull(message = "Payed by is required")
        Long payedBy,

        @NotBlank(message = "Description is required")
        @Size(min = 10, max = 500, message = "Description must be between 10 and 500 characters")
        String description,

        @NotNull(message = "Amount is required")
        @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
        BigDecimal amount
) {
}
