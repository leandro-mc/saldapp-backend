package com.lmora.saldapp.application.usecase;

import com.lmora.saldapp.application.port.in.GroupUseCase;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.domain.exception.ResourceNotFoundException;
import com.lmora.saldapp.domain.model.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GroupUseCaseImpl implements GroupUseCase {
    private final GroupRepositoryPort groupRepository;

    @Override
    public Group create(Group group) {
        return groupRepository.save(group);
    }

    @Override
    public Group update(Long id, Group group) {
        // Verify that the group exists before updating
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));

        // Verify that the group is not closed
        existingGroup.validateIsOpen();

        // Only update the name and description, as other fields like 'closed' should not be updated here
        existingGroup.setName(group.getName());
        existingGroup.setDescription(group.getDescription());

        return groupRepository.save(existingGroup);
    }

    @Override
    public Group close(Long id) {
        Group existingGroup = groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));

        existingGroup.validateIsOpen();

        existingGroup.close();
        return groupRepository.save(existingGroup);
    }

    @Override
    public Group findById(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Group", id));
    }
}
