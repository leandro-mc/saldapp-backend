package com.lmora.saldapp.adapter.out.persistence.adapter;

import com.lmora.saldapp.adapter.out.persistence.mapper.GroupEntityMapper;
import com.lmora.saldapp.adapter.out.persistence.repository.GroupJpaRepository;
import com.lmora.saldapp.application.port.out.GroupRepositoryPort;
import com.lmora.saldapp.domain.model.Group;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class GroupRepositoryAdapter implements GroupRepositoryPort {

    private final GroupJpaRepository groupJpaRepository;
    private final GroupEntityMapper groupMapper;

    @Override
    public Group save(Group group) {
        return groupMapper.toDomain(
                groupJpaRepository.save(
                        groupMapper.toEntity(group)
                )
        );
    }

    @Override
    public Optional<Group> findById(Long id) {
        return groupJpaRepository.findById(id)
                .map(groupMapper::toDomain);

    }
}
