package com.lmora.saldapp.adapter.in.web.dto.integrant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record IntegrantRequest(
        @NotBlank(message = "Name is required")
        @Size(min = 1, max = 200, message = "Integrant name must be between 1 and 200 characters")
        String name
) {
}
