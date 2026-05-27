package com.lmora.saldapp.application.usecase;

import com.lmora.saldapp.application.port.in.IntegrantUseCase;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.domain.exception.IntegrantDontBelongToGroupException;
import com.lmora.saldapp.domain.exception.ResourceNotFoundException;
import com.lmora.saldapp.domain.model.Group;
import com.lmora.saldapp.domain.model.Integrant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class IntegrantUseCaseImpl implements IntegrantUseCase {

    private final IntegrantRepositoryPort integrantRepository;
    private final GroupRepositoryPort groupRepository;

    @Transactional
    @Override
    public Integrant createAndAssign(Long groupId, Integrant integrant) {
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        existingGroup.validateIsOpen();

        integrant.setGroupId(groupId);
        return integrantRepository.save(integrant);
    }

    @Transactional
    @Override
    public Integrant update(Long groupId, Long integrantId, Integrant integrant) {
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        existingGroup.validateIsOpen();

        Integrant existingIntegrant = integrantRepository.findById(integrantId)
                .orElseThrow(() -> new ResourceNotFoundException("Integrant", integrantId));

        // SetChangedValues
        existingIntegrant.setName(integrant.getName());

        return integrantRepository.save(existingIntegrant);
    }

    @Transactional
    @Override
    public void delete(Long groupId, Long integrantId) {
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        existingGroup.validateIsOpen();

        Integrant existingIntegrant = integrantRepository.findById(integrantId)
                .orElseThrow(() -> new ResourceNotFoundException("Integrant", integrantId));

        // Verify that the integrant belongs to the group before deleting
        if (!Objects.equals(existingIntegrant.getGroupId(), groupId)) {
            throw new IntegrantDontBelongToGroupException(integrantId, groupId);
        }

        // TODO: Verify that the integrant does not have associated expenses before deleting

        integrantRepository.deleteById(integrantId);
    }

    @Override
    public List<Integrant> findAllByGroup(Long groupId) {
        return integrantRepository.findAllByGroup(groupId);
    }

}
