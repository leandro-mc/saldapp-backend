package com.lmora.saldapp.adapter.in.web.dto.group;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record GroupResponse(
        Long id,
        String name,
        String description,
        String currency,
        int integrantCount,
        BigDecimal totalExpenses,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
