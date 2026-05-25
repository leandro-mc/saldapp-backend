package com.lmora.saldapp.adapter.in.web;

import com.lmora.saldapp.adapter.in.web.dto.group.CreateGroupRequest;
import com.lmora.saldapp.adapter.in.web.dto.group.GroupResponse;
import com.lmora.saldapp.adapter.in.web.dto.group.UpdateGroupRequest;
import com.lmora.saldapp.adapter.in.web.mapper.GroupWebMapper;
import com.lmora.saldapp.application.port.in.GroupUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/groups")
public class GroupController {
    private final GroupWebMapper mapper;
    private final GroupUseCase useCase;

    @PostMapping
    ResponseEntity<GroupResponse> createGroup(@Valid @RequestBody CreateGroupRequest request) {
        GroupResponse response = mapper.domainToDto(
                useCase.create(mapper.dtoToDomain(request))
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{id}")
    ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @Valid @RequestBody UpdateGroupRequest request) {
        GroupResponse response = mapper.domainToDto(
                useCase.update(id, mapper.dtoToDomain(request))
        );
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    ResponseEntity<GroupResponse> findById(@PathVariable Long id) {
        GroupResponse response = mapper.domainToDto(
                useCase.findById(id)
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/close")
    ResponseEntity<GroupResponse> close(@PathVariable Long id) {
        GroupResponse response = mapper.domainToDto(
                useCase.close(id)
        );
        return ResponseEntity.ok(response);
    }
}
