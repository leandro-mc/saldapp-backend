package com.lmora.saldapp.application.usecase.group.model;

import com.lmora.saldapp.domain.model.Group;

import java.math.BigDecimal;

public record GroupDetailsResult(
        Group group, int integrantCount,
        BigDecimal totalExpenses
) {
}
