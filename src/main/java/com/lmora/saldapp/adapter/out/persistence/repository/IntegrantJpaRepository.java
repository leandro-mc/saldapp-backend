package com.lmora.saldapp.adapter.out.persistence.repository;

import com.lmora.saldapp.adapter.out.persistence.entity.IntegrantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface IntegrantJpaRepository extends JpaRepository<IntegrantEntity, Long> {
    List<IntegrantEntity> findByGroupId(Long groupId);
    int  countByGroupId(Long groupId);
}
