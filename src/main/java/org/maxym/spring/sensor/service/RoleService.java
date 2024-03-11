package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.RoleNotFoundException;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.repository.RoleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Cacheable(value = "allRoles")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "roleByName", key = "#role")
    public Role findByRole(String role) {
        return roleRepository.findByRole(role)
                .orElseThrow(() -> new RoleNotFoundException(format("Role %s not found.", role)));
    }
}
