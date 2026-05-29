package com.lmora.saldapp.application.usecase;

import com.lmora.saldapp.application.port.in.IntegrantUseCase;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.domain.exception.IntegrantDontBelongToGroupException;
import com.lmora.saldapp.domain.exception.IntegrantHasExpensesException;
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
    private final ExpenseRepositoryPort expenseRepository;

    @Transactional
    @Override
    public Integrant createAndAssign(Long groupId, Integrant integrant) {
        Group existingGroup = findOpenGroup(groupId);

        integrant.setGroupId(existingGroup.getId());
        return integrantRepository.save(integrant);
    }

    @Transactional
    @Override
    public Integrant update(Long groupId, Long integrantId, Integrant integrant) {
        Group existingGroup = findOpenGroup(groupId);

        Integrant existingIntegrant = findAndValidateIntegrant(existingGroup.getId(), integrantId);

        // SetChangedValues
        existingIntegrant.setName(integrant.getName());

        return integrantRepository.save(existingIntegrant);
    }

    @Transactional
    @Override
    public void delete(Long groupId, Long integrantId) {
        Group existingGroup = findOpenGroup(groupId);

        Integrant existingIntegrant = findAndValidateIntegrant(existingGroup.getId(), integrantId);

        if(!expenseRepository.findAllByIntegrant(existingIntegrant.getId()).isEmpty()) {
            throw new IntegrantHasExpensesException(existingIntegrant.getId());
        }

        integrantRepository.deleteById(integrantId);
    }

    @Override
    public List<Integrant> findAllByGroup(Long groupId) {
        groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));

        return integrantRepository.findAllByGroup(groupId);
    }

    private Group findOpenGroup(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));
        // Verify that the group is not closed
        existingGroup.validateIsOpen();
        return existingGroup;
    }

    private Integrant findAndValidateIntegrant(Long groupId, Long integrantId) {
        Integrant existingIntegrant = integrantRepository.findById(integrantId)
                .orElseThrow(() -> new ResourceNotFoundException("Integrant", integrantId));

        // Verify that the integrant belongs to the group
        if (!Objects.equals(existingIntegrant.getGroupId(), groupId)) {
            throw new IntegrantDontBelongToGroupException(existingIntegrant.getId(), groupId);
        }
        return existingIntegrant;
    }

}
