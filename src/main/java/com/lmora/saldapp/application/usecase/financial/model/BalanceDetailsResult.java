package com.lmora.saldapp.application.usecase.financial.model;

import com.lmora.saldapp.domain.model.Balance;

public record BalanceDetailsResult(
        Balance balance,
        String integrantName
) {
}
