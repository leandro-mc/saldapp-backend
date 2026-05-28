package com.lmora.saldapp.adapter.in.web.dto.financial;

import java.math.BigDecimal;

public record SettlementResponse(
        Long fromIntegrantId,
        Long toIntegrantId,
        BigDecimal amount
) {
}
