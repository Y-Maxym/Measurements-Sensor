package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(defaultRoles());
        userRepository.save(user);
    }

    @Transactional
    public void grantRole(Role role, User user) {
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    public void takeRole(Role role, User user) {
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    public boolean hasRole(Role role, User user) {
        return user.getRoles().contains(role);
    }

    private Set<Role> defaultRoles() {
        return roleService.findByRole("ROLE_USER")
                .stream()
                .collect(Collectors.toSet());
    }
}
