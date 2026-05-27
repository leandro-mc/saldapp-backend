package com.lmora.saldapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Expense {
    private Long id;
    private Long groupId;
    private Long payedBy;
    private String description;
    private BigDecimal amount;
    private LocalDateTime date;
    private LocalDateTime createdAt;
}
