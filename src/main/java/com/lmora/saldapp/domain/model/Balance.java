package com.lmora.saldapp.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Balance {
    private Long integrantId;
    private BigDecimal totalPaid;
    private BigDecimal balance; // positive: the integrant should receive money, negative: the integrant should pay money
}
