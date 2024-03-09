package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.repository.RoleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;

    @Cacheable(value = "allRoles")
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Cacheable(value = "roleByName", key = "#name")
    public Optional<Role> findByRole(String name) {
        return roleRepository.findByRole(name);
    }
}
