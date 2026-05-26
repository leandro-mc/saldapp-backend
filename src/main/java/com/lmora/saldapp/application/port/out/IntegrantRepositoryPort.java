package com.lmora.saldapp.application.port.out;

import com.lmora.saldapp.domain.model.Integrant;

import java.util.List;
import java.util.Optional;

public interface IntegrantRepositoryPort {
    Integrant save(Integrant integrant);
    List<Integrant> findAllByGroup(Long groupId);
    Optional<Integrant> findById(Long id);
    void deleteById(Long id);
}
