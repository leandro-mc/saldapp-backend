package com.lmora.saldapp.adapter.out.persistence.repository;

import com.lmora.saldapp.adapter.out.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, Long> {
    List<ExpenseEntity> findByPayedById(Long payedById);
    List<ExpenseEntity> findByGroupId(Long groupId);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM ExpenseEntity e WHERE e.group.id = :groupId")
    BigDecimal sumExpenseAmountByGroupId(@Param("groupId") Long groupId);
}