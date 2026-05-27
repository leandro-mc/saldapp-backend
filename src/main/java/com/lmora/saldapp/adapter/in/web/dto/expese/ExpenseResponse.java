package com.lmora.saldapp.adapter.in.web.dto.expese;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ExpenseResponse(
        Long id,
        Long groupId,
        Long payedBy,
        String description,
        BigDecimal amount,
        LocalDateTime date
) {
}
