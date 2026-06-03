package com.lmora.saldapp.application.usecase.group.model;

import com.lmora.saldapp.domain.model.Group;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class GroupDetailsResult {
    private final Group group;
    private final int integrantCount;
    private final BigDecimal totalExpenses;

    public GroupDetailsResult(Group group, int integrantCount, BigDecimal totalExpenses) {
        this.group = group;
        this.integrantCount = integrantCount;
        this.totalExpenses = totalExpenses;
    }
}
