package com.lmora.saldapp.adapter.out.persistence.repository;

import com.lmora.saldapp.adapter.out.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByPayedById(Long payedById);
    List<ExpenseEntity> findByGroupId(Long groupId);
}
