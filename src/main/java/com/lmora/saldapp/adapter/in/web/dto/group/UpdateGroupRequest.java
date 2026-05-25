package com.lmora.saldapp.adapter.in.web.dto.group;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public record UpdateGroupRequest(
        @NotNull(message = "Group ID is required")
        Long id,

        @NotBlank(message = "Group name is required")
        @Size(min = 1, max = 200, message = "Group name must be between 1 and 200 characters")
        String name,

        @Size(max = 500, message = "Group description must be at most 500 characters")
        String description
) {
}
