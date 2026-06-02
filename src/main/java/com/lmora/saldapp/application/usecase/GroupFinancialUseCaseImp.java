package com.lmora.saldapp.application.usecase;

import com.lmora.saldapp.application.port.in.GroupFinancialUseCase;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
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
    public List<Balance> getGroupBalances(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        return calculateGroupBalances(existingGroup.getId());
    }

    @Override
    public Balance getIntegrantBalance(Long groupId, Long integrantId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        // Verify that the integrant exists and belongs to the group
        Integrant existingIntegrant = findAndValidateIntegrant(existingGroup.getId(), integrantId);

        return calculateGroupBalances(groupId).stream()
                .filter(balance -> Objects.equals(balance.getIntegrantId(), existingIntegrant.getId()))
                .findFirst()
                .orElse(new Balance(existingIntegrant.getId(), BigDecimal.ZERO));
    }

    @Override
    public List<Transaction> getGroupTransactions(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        return SettlementCalculator.calculate(calculateGroupBalances(existingGroup.getId()));
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

        // Everyone starts debt,then we add the expenses they paid
        Map<Long, BigDecimal> groupBalances = new HashMap<>();
        for (Long id : integrantsIds) {
            groupBalances.put(id, equalShare.negate());
        }

        // Add the expenses paid by each integrant to their balance
        for (Expense expense : groupExpenses) {
            groupBalances.merge(expense.getPayedBy(), expense.getAmount(), BigDecimal::add);
        }

        return groupBalances.entrySet().stream()
                .map(entry -> new Balance(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
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
