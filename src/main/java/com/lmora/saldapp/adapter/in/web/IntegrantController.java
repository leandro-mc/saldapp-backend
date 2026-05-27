package com.lmora.saldapp.adapter.in.web;

import com.lmora.saldapp.adapter.in.web.dto.group.CreateGroupRequest;
import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantRequest;
import com.lmora.saldapp.adapter.in.web.dto.integrant.IntegrantResponse;
import com.lmora.saldapp.adapter.in.web.mapper.IntegrantWebMapper;
import com.lmora.saldapp.application.port.in.IntegrantUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups/{groupId}/integrants")
public class IntegrantController {
    private final IntegrantUseCase useCase;
    private final IntegrantWebMapper mapper;

    @PostMapping
    ResponseEntity<IntegrantResponse> createIntegrant(
            @Valid @RequestBody IntegrantRequest request,
            @PathVariable Long groupId)
    {
        IntegrantResponse response = mapper.toDto(
                useCase.createAndAssign(groupId, mapper.toDomain(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    ResponseEntity<IntegrantResponse> updateIntegrant(
            @Valid @RequestBody IntegrantRequest request,
            @PathVariable Long groupId,
            @PathVariable Long id)
    {
        IntegrantResponse response = mapper.toDto(
                useCase.update(groupId, id, mapper.toDomain(request))
        );
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteIntegrant(
            @PathVariable Long groupId,
            @PathVariable Long id)
    {
        useCase.delete(groupId, id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    ResponseEntity<List<IntegrantResponse>> findAllIntegrantsByGroup(@PathVariable Long groupId) {
        List<IntegrantResponse> response = useCase.findAllByGroup(groupId).stream()
                .map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(response);
    }

}
