package com.lmora.saldapp.application.port.in;

import com.lmora.saldapp.domain.model.Balance;
import com.lmora.saldapp.domain.model.Transaction;

import java.util.List;

public interface GroupFinancialUseCase {
    List<Balance> getGroupBalances(Long groupId);
    Balance getIntegrantBalance(Long groupId, Long integrantId);
    List<Transaction> getGroupTransactions(Long groupId);
}
