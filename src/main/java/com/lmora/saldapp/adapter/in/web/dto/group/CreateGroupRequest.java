package com.lmora.saldapp.adapter.in.web.dto.group;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record CreateGroupRequest(
        @NotBlank(message = "Group name is required")
        @Size(min = 1, max = 200, message = "Group name must be between 1 and 200 characters")
        String name,

        @Size(max = 500, message = "Group description must be at most 500 characters")
        String description,

        @NotBlank(message = "Currency is required")
        @Size(max = 5, message = "Currency code must be at most 5 characters")
        String currency,

        @NotNull(message = "Start date is required")
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")      // ISO 8601 format
        LocalDateTime startDate
) {
}
