package com.lmora.saldapp.application.port.in;

import com.lmora.saldapp.application.usecase.financial.model.BalanceDetailsResult;
import com.lmora.saldapp.domain.model.Transaction;

import java.util.List;

public interface GroupFinancialUseCase {
    List<BalanceDetailsResult> getGroupBalances(Long groupId);
    BalanceDetailsResult getIntegrantBalance(Long groupId, Long integrantId);
    List<Transaction> getGroupTransactions(Long groupId);
}
