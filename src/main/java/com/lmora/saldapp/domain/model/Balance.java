package com.lmora.saldapp.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Balance {
    private Long integrantId;
    private BigDecimal balance; // positivo = le deben, negativo = debe
}
