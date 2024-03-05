package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.repository.RoleRepository;
import org.maxym.spring.sensor.util.responce.exception.RoleNotFoundException;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(RoleNotFoundException::new);
    }
}
