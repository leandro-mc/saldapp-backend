package com.lmora.saldapp.adapter.in.web.dto.financial;

import java.math.BigDecimal;

public record SettlementResponse(
        Long fromIntegrantId,
        String fromIntegrantName,
        Long toIntegrantId,
        String toIntegrantName,
        BigDecimal amount
) {
}
