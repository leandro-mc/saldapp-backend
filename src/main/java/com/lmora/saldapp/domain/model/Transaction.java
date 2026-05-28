package com.lmora.saldapp.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class Transaction {
    private Long fromIntegrantId;
    private Long toIntegrantId;
    private BigDecimal amount;
}
