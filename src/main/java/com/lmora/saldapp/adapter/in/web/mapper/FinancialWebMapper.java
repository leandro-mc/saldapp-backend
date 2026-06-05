package com.lmora.saldapp.adapter.in.web.mapper;

import com.lmora.saldapp.adapter.in.web.dto.financial.BalanceResponse;
import com.lmora.saldapp.adapter.in.web.dto.financial.SettlementResponse;
import com.lmora.saldapp.application.usecase.financial.model.BalanceDetailsResult;
import com.lmora.saldapp.domain.model.Transaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FinancialWebMapper {
    SettlementResponse toDto(Transaction transaction);

    @Mapping(source = "balance.integrantId", target = "integrantId")
    @Mapping(source = "balance.totalPaid", target = "totalPaid")
    @Mapping(source = "balance.balance", target = "balance")
    BalanceResponse toDto(BalanceDetailsResult balance);
}
