package com.lmora.saldapp.application.port.in;

import com.lmora.saldapp.domain.model.Group;

public interface GroupUseCase {
    Group create(Group group);
    Group update(Long id, Group group);
    Group close(Long id);
    Group findById(Long id);
}
