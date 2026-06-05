package com.lmora.saldapp.application.usecase.group;

import com.lmora.saldapp.application.port.in.GroupUseCase;
import com.lmora.saldapp.application.port.out.ExpenseRepositoryPort;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.application.usecase.group.model.GroupDetailsResult;
import com.lmora.saldapp.domain.exception.ResourceNotFoundException;
import com.lmora.saldapp.domain.model.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GroupUseCaseImpl implements GroupUseCase {

    private final GroupRepositoryPort groupRepository;
    private final IntegrantRepositoryPort integrantRepository;
    private final ExpenseRepositoryPort expenseRepository;

    @Transactional
    @Override
    public GroupDetailsResult create(Group group) {
        return enrichWithDetails(groupRepository.save(group));
    }

    @Transactional
    @Override
    public GroupDetailsResult update(Long id, Group group) {
        // Verify that the group exists before updating
        Group existingGroup = findOpenGroup(id);

        // Only update the name and description, as other fields like 'closed' should not be updated here
        existingGroup.setName(group.getName());
        existingGroup.setDescription(group.getDescription());
        existingGroup.setCurrency(group.getCurrency());

        return enrichWithDetails(groupRepository.save(existingGroup));
    }

    @Transactional
    @Override
    public GroupDetailsResult close(Long id) {
        Group existingGroup = findOpenGroup(id);
        existingGroup.close();
        return enrichWithDetails(groupRepository.save(existingGroup));
    }

    @Override
    public GroupDetailsResult findById(Long id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));


        return enrichWithDetails(group);
    }

    private Group findOpenGroup(Long groupId) {
        // Verify that the group exists
        Group existingGroup = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group", groupId));
        // Verify that the group is not closed
        existingGroup.validateIsOpen();
        return existingGroup;
    }

    private GroupDetailsResult enrichWithDetails(Group group) {
        int integrantCount = integrantRepository.countByGroupId(group.getId());
        BigDecimal totalExpenses = expenseRepository.sumAmountByGroupId(group.getId());

        return new GroupDetailsResult(group, integrantCount, totalExpenses);
    }
}
