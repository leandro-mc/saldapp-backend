package com.lmora.saldapp.adapter.in.web;

import com.lmora.saldapp.adapter.in.web.dto.financial.BalanceResponse;
import com.lmora.saldapp.adapter.in.web.dto.financial.SettlementResponse;
import com.lmora.saldapp.adapter.in.web.mapper.FinancialWebMapper;
import com.lmora.saldapp.application.port.in.GroupFinancialUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/groups/{groupId}")
public class FinancialController {

    private final FinancialWebMapper mapper;
    private final GroupFinancialUseCase useCase;

    @GetMapping("/balances")
    ResponseEntity<List<BalanceResponse>> findAllBalancesByGroupId(@Validated @PathVariable Long groupId) {
        List<BalanceResponse> balances = useCase.getGroupBalances(groupId).stream()
                .map(mapper::toDto)
                .toList();

        return ResponseEntity.ok(balances);
    }

    @GetMapping("/balances/{integrantId}")
    ResponseEntity<BalanceResponse> findBalanceByGroupIdAndIntegrantId(
            @Validated @PathVariable Long groupId,
            @Validated @PathVariable Long integrantId
    ) {
        BalanceResponse balance = mapper.toDto(
                useCase.getIntegrantBalance(groupId, integrantId)
        );

        return ResponseEntity.ok(balance);
     }

     @GetMapping("/settlements")
    ResponseEntity<List<SettlementResponse>> findAllSettlementsByGroupIdAndGroupId(
            @Validated @PathVariable Long groupId
    ) {
        List<SettlementResponse> settlements = useCase.getGroupTransactions(groupId).stream()
                .map(mapper::toDto)
                .toList();

        return ResponseEntity.ok(settlements);
     }

}
