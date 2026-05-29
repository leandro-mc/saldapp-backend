package com.lmora.saldapp.application.usecase;

import com.lmora.saldapp.application.port.in.ExpenseUseCase;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.domain.exception.ExpenseDontBelongToGroupException;
import com.lmora.saldapp.domain.exception.IntegrantDontBelongToGroupException;
import com.lmora.saldapp.domain.exception.ResourceNotFoundException;
import com.lmora.saldapp.domain.model.Expense;
import com.lmora.saldapp.domain.model.Group;
import com.lmora.saldapp.domain.model.Integrant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ExpenseUseCaseImpl implements ExpenseUseCase {

    private final ExpenseRepositoryPort expenseRepository;
    private final GroupRepositoryPort groupRepository;
    private final IntegrantRepositoryPort integrantRepository;

    @Transactional
    @Override
    public Expense createAndAssign(Long groupId, Expense expense) {
        Group existingGroup = findOpenGroup(groupId);
        expense.setGroupId(existingGroup.getId());

        findAndValidateIntegrant(groupId, expense.getPayedBy());

        return expenseRepository.save(expense);
    }

    @Transactional
    @Override
    public Expense update(Long groupId, Long expenseId, Expense expense) {
        Group existingGroup = findOpenGroup(groupId);

        Expense existingExpense = findAndValidateExpense(existingGroup.getId(), expenseId);

        Integrant existingIntegrant = findAndValidateIntegrant(existingGroup.getId(), expense.getPayedBy());

        // Set to Update
        existingExpense.setPayedBy(existingIntegrant.getId());
        existingExpense.setDescription(expense.getDescription());
        existingExpense.setAmount(expense.getAmount());

        return expenseRepository.save(existingExpense);
    }

    @Transactional
    @Override
    public void delete(Long groupId, Long expenseId) {
        Group existingGroup = findOpenGroup(groupId);
        Expense existingExpense = findAndValidateExpense(existingGroup.getId(), expenseId);

        expenseRepository.deleteById(existingExpense.getId());
    }

    @Override
    public List<Expense> findAllByIntegrant(Long groupId, Long integrantId) {
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        findAndValidateIntegrant(existingGroup.getId(), integrantId);
        return expenseRepository.findAllByIntegrant(integrantId);
    }

    private Group findOpenGroup(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));
        // Verify that the group is not closed
        existingGroup.validateIsOpen();
        return existingGroup;
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

    private Expense findAndValidateExpense(Long groupId, Long expenseId) {
        Expense existingExpense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense", expenseId));

        // Verify that the expense belongs to the group
        if (!Objects.equals(existingExpense.getGroupId(), groupId)) {
            throw new ExpenseDontBelongToGroupException(existingExpense.getId(), groupId);
        }
        return existingExpense;
    }
}
