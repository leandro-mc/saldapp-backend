package com.lmora.saldapp.application.port.out;

import com.lmora.saldapp.domain.model.Integrant;

import java.util.List;

public interface IntegrantRepositoryPort {
    Integrant save(Integrant integrant);
    List<Integrant> findAllByGroup(Long groupId);
    void deleteById(Long id);
}
