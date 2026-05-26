package com.lmora.saldapp.adapter.out.persistence.adapter;

import com.lmora.saldapp.adapter.out.persistence.entity.IntegrantEntity;
import com.lmora.saldapp.adapter.out.persistence.mapper.IntegrantEntityMapper;
import com.lmora.saldapp.adapter.out.persistence.repository.IntegrantJpaRepository;
import com.lmora.saldapp.application.port.out.IntegrantRepositoryPort;
import com.lmora.saldapp.domain.model.Integrant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class IntegrantRepositoryAdapter implements IntegrantRepositoryPort {

    private final IntegrantJpaRepository integrantJpaRepository;
    private final IntegrantEntityMapper integrantMapper;

    @Override
    public Integrant save(Integrant integrant) {
        return integrantMapper.toDomain(
                integrantJpaRepository.save(
                        integrantMapper.toEntity(integrant)
                )
        );
    }

    @Override
    public List<Integrant> findByGroupId(Long groupId) {
        List<IntegrantEntity> integrantEntities = integrantJpaRepository.findByGroupId(groupId);

        return integrantEntities.stream()
                .map(integrantMapper::toDomain)
                .toList();
    }

    @Override
    public void deleteById(Long id) {
        integrantJpaRepository.deleteById(id);
    }
}
