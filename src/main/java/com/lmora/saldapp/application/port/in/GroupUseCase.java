package com.lmora.saldapp.application.port.in;

import com.lmora.saldapp.application.usecase.group.model.GroupDetailsResult;
import com.lmora.saldapp.domain.model.Group;

public interface GroupUseCase {
    GroupDetailsResult create(Group group);
    GroupDetailsResult update(Long id, Group group);
    GroupDetailsResult close(Long id);
    GroupDetailsResult findById(Long id);
}
