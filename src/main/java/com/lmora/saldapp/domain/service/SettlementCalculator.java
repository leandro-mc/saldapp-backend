package com.lmora.saldapp.domain.service;


import com.lmora.saldapp.domain.model.Balance;
import com.lmora.saldapp.domain.model.Transaction;

import java.math.BigDecimal;
import java.util.*;

public class SettlementCalculator {
    public static List<Transaction> calculate(List<Balance> balances) {
        if (balances == null) throw new IllegalArgumentException("balances can't be null");

        for (Balance balance : balances) {
            if (balance == null) throw new IllegalArgumentException("balance can't be null");
            if (balance.getBalance() == null || balance.getIntegrantId() == null) throw new IllegalArgumentException("balance's fields can't be null");
        }

        List<Transaction> transactions = new ArrayList<>();

        PriorityQueue<Balance> debtors = new PriorityQueue<>(
                Comparator.comparing(Balance::getBalance)
        ); // Order of most negative to least negative (lowest to highest)

        PriorityQueue<Balance> creditors = new PriorityQueue<>(
                Comparator.comparing(Balance::getBalance).reversed()
        ); // Order of most positive to least positive (highest to lowest)

        for (Balance balance : balances) {
            if (balance.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(balance);
            } else if (balance.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(balance);
            }
        }

        while (!debtors.isEmpty() && !creditors.isEmpty()) {
            Balance debtor = debtors.poll();
            Balance creditor = creditors.poll();

            BigDecimal amount = debtor.getBalance().abs().min(Objects.requireNonNull(creditor).getBalance());
            debtor.setBalance(debtor.getBalance().add(amount));
            creditor.setBalance(creditor.getBalance().subtract(amount));

            transactions.add(new  Transaction(debtor.getIntegrantId(), creditor.getIntegrantId(), amount));

            if (debtor.getBalance().compareTo(BigDecimal.ZERO) < 0) {
                debtors.add(debtor);
            } else if (creditor.getBalance().compareTo(BigDecimal.ZERO) > 0) {
                creditors.add(creditor);
            }
        }

        return transactions;
    }
}
