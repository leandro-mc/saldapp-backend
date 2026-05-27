package com.lmora.saldapp.adapter.out.persistence.repository;

import com.lmora.saldapp.adapter.out.persistence.entity.ExpenseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseJpaRepository extends JpaRepository<ExpenseEntity, Long> {
}
