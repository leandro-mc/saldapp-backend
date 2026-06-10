package com.lmora.saldapp.application.usecase.financial;

import com.lmora.saldapp.application.port.in.GroupFinancialUseCase;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.application.usecase.financial.model.BalanceDetailsResult;
import com.lmora.saldapp.application.usecase.financial.model.TransactionDetailsResult;
import com.lmora.saldapp.domain.exception.IntegrantDontBelongToGroupException;
import com.lmora.saldapp.domain.exception.ResourceNotFoundException;
import com.lmora.saldapp.domain.model.*;
import com.lmora.saldapp.domain.service.SettlementCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupFinancialUseCaseImp implements GroupFinancialUseCase {

    private final ExpenseRepositoryPort expenseRepository;
    private final GroupRepositoryPort groupRepository;
    private final IntegrantRepositoryPort integrantRepository;

    @Override
    public List<BalanceDetailsResult> getGroupBalances(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        List<Balance> balances = calculateGroupBalances(existingGroup.getId());

        List<Integrant> integrants = integrantRepository.findAllByGroup(existingGroup.getId());

        List<BalanceDetailsResult> balanceDetailsResults = new ArrayList<>();
        for (Balance balance : balances) {
            Integrant integrant = integrants.stream()
                    .filter(i -> Objects.equals(i.getId(), balance.getIntegrantId()))
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException("Integrant", balance.getIntegrantId()));
            balanceDetailsResults.add(new BalanceDetailsResult(balance, integrant.getName()));
        }
        return balanceDetailsResults;
    }

    @Override
    public BalanceDetailsResult getIntegrantBalance(Long groupId, Long integrantId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        // Verify that the integrant exists and belongs to the group
        Integrant existingIntegrant = findAndValidateIntegrant(existingGroup.getId(), integrantId);

        Balance integrantBalance = calculateIntegrantBalance(existingGroup.getId(), existingIntegrant.getId());

        return new BalanceDetailsResult(integrantBalance, existingIntegrant.getName());
    }

    @Override
    public List<TransactionDetailsResult> getGroupTransactions(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        List<Transaction> transactions = SettlementCalculator.calculate(calculateGroupBalances(existingGroup.getId()));

        Map<Long, String> integrantNames = integrantRepository.findAllByGroup(existingGroup.getId()).stream()
                .collect(Collectors.toMap(Integrant::getId, Integrant::getName));

        return transactions.stream()
                .map(transaction -> new TransactionDetailsResult(
                        transaction,
                        integrantNames.getOrDefault(transaction.getFromIntegrantId(), "Unknown"),
                        integrantNames.getOrDefault(transaction.getToIntegrantId(), "Unknown")
                ))
                .toList();
    }

    private List<Balance> calculateGroupBalances(Long groupId) {

        List<Long> integrantsIds = integrantRepository.findAllByGroup(groupId)
                .stream()
                .map(Integrant::getId)
                .toList();

        if (integrantsIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Expense> groupExpenses = expenseRepository.findAllByGroup(groupId);

        BigDecimal totalExpenses = groupExpenses.stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal equalShare = totalExpenses
                .divide(BigDecimal.valueOf(integrantsIds.size()), 2, RoundingMode.HALF_UP);

        // Everyone starts debt,then we add the expenses they paid and sum total paid by each integrant to calculate the balance
        Map<Long, BigDecimal> groupBalances = new HashMap<>();
        Map<Long, BigDecimal> totalPaidByIntegrant = new HashMap<>();
        for (Long id : integrantsIds) {
            groupBalances.put(id, equalShare.negate());
            totalPaidByIntegrant.put(id, BigDecimal.ZERO);
        }

        // Add the expenses paid by each integrant to their balance
        for (Expense expense : groupExpenses) {
            groupBalances.merge(expense.getPayedBy(), expense.getAmount(), BigDecimal::add);
            totalPaidByIntegrant.merge(expense.getPayedBy(), expense.getAmount(), BigDecimal::add);
        }

        return groupBalances.entrySet().stream()
                .map(entry -> new Balance(
                        entry.getKey(),
                        totalPaidByIntegrant.get(entry.getKey()),
                        entry.getValue()))
                .collect(Collectors.toList());
    }

    private Balance calculateIntegrantBalance(Long groupId, Long integrantId) {

        BigDecimal totalIntegrantExpenses = expenseRepository.findAllByIntegrant(integrantId).stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalGroupExpenses = expenseRepository.sumAmountByGroupId(groupId);
        int integrantCount = integrantRepository.countByGroupId(groupId);

        BigDecimal equalShare = totalGroupExpenses
                .divide(BigDecimal.valueOf(integrantCount), 2, RoundingMode.HALF_UP);

        return new Balance(
                integrantId,
                totalIntegrantExpenses,
                equalShare.negate().add(totalIntegrantExpenses)
        );
    }

    private Integrant findAndValidateIntegrant(Long groupId, Long integrantId) {
        Integrant existingIntegrant = integrantRepository.findById(integrantId)
                .orElseThrow(() -> new ResourceNotFoundException("Integrant", integrantId));

        // Verify that the integrant belongs to the group
        if (!Objects.equals(existingIntegrant.getGroupId(), groupId)) {
            throw new IntegrantDontBelongToGroupException(existingIntegrant.getId(), groupId);
        }
        return existingIntegrant;
    }
}
