package com.lmora.saldapp.application.usecase.financial.model;

import com.lmora.saldapp.domain.model.Transaction;

public record TransactionDetailsResult(
        Transaction transaction,
        String fromIntegrantName,
        String toIntegrantName
) {
}
