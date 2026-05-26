package com.lmora.saldapp.application.port.in;

import com.lmora.saldapp.domain.model.Integrant;

import java.util.List;

public interface IntegrantUseCase {
    Integrant createAndAssign(Long groupId, Integrant integrant);
    Integrant update(Long groupId, Long integrantId, Integrant integrant);
    void delete(Long groupId, Long integrantId);
    List<Integrant> findAllByGroup(Long groupId);
}
