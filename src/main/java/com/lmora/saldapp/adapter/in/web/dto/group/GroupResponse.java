package com.lmora.saldapp.adapter.in.web.dto.group;

import java.time.LocalDateTime;

public record GroupResponse(
        Long id,
        String name,
        String description,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
