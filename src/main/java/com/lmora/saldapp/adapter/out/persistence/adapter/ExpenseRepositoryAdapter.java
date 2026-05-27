package com.lmora.saldapp.adapter.out.persistence.adapter;

import com.lmora.saldapp.adapter.out.persistence.entity.ExpenseEntity;
import com.lmora.saldapp.adapter.out.persistence.entity.GroupEntity;
import com.lmora.saldapp.adapter.out.persistence.entity.IntegrantEntity;
import com.lmora.saldapp.adapter.out.persistence.mapper.ExpenseEntityMapper;
import com.lmora.saldapp.adapter.out.persistence.repository.ExpenseJpaRepository;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.domain.model.Expense;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ExpenseRepositoryAdapter implements ExpenseRepositoryPort {

    private final ExpenseJpaRepository expenseJpaRepository;
    private final ExpenseEntityMapper expenseMapper;

    @Override
    public Expense save(Expense expense) {
        ExpenseEntity expenseEntity = expenseMapper.toEntity(expense);

        // Resolve PK for group
        GroupEntity group = new GroupEntity();
        group.setId(expense.getGroupId());
        expenseEntity.setGroup(group);

        // Resolve PK for payedBy
        IntegrantEntity payedBy = new IntegrantEntity();
        payedBy.setId(expense.getPayedBy());
        expenseEntity.setPayedBy(payedBy);

        return expenseMapper.toDomain(
                expenseJpaRepository.save(expenseEntity)
        );
    }

    @Override
    public void deleteById(Long id) {
        expenseJpaRepository.deleteById(id);
    }

    @Override
    public Optional<Expense> findById(Long id) {
        return expenseJpaRepository.findById(id)
                .map(expenseMapper::toDomain);
    }

    @Override
    public List<Expense> findAllByIntegrant(Long integrantId) {
        List<ExpenseEntity> expenseEntities = expenseJpaRepository.findByPayedById(integrantId);

        return expenseEntities.stream()
                .map(expenseMapper::toDomain)
                .collect(Collectors.toList());
    }
}
