package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.financial.BalanceResponse;
import com.lmora.saldapp.adapter.in.web.dto.financial.SettlementResponse;
import com.lmora.saldapp.domain.model.Balance;
import com.lmora.saldapp.domain.model.Transaction;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FinancialWebMapper {
    SettlementResponse toDto(Transaction transaction);
    BalanceResponse toDto(Balance balance);
}
