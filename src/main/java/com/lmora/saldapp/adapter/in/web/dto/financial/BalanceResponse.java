package com.lmora.saldapp.adapter.in.web.dto.financial;

import java.math.BigDecimal;

public record BalanceResponse(
        Long integrantId,
        BigDecimal balance  // positive: the integrant should receive money, negative: the integrant should pay money
) {
}
