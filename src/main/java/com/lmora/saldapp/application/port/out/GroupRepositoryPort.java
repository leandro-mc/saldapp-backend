package com.lmora.saldapp.application.port.out;

import com.lmora.saldapp.domain.model.Group;

import java.util.List;
import java.util.Optional;

public interface GroupRepositoryPort {
    Group save(Group group);
    Optional<Group> findById(Long id);

    // Other methods like delete can be added as needed
}
