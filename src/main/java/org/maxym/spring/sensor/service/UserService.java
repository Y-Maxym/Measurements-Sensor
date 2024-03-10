package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.UserRepository;
import org.maxym.spring.sensor.security.model.AuthDetails;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Cacheable("allUsers")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable(value = "userById", key = "#id")
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Cacheable(value = "userByUsername", key = "#username")
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Cacheable(value = "userByEmail", key = "#email")
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails principal = (AuthDetails) authentication.getPrincipal();
        return principal.user();
    }

    @Transactional

    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),

    }, put = {
            @CachePut(value = "userById", key = "#user.id"),
            @CachePut(value = "userByUsername", key = "#user.username"),
            @CachePut(value = "userByEmail", key = "#user.email")
    })
    @SuppressWarnings("all")
    public Optional<User> save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(defaultRoles());
        return Optional.of(userRepository.save(user));
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "userById", key = "#user.id"),
            @CacheEvict(value = "userByUsername", key = "#user.username"),
            @CacheEvict(value = "userByEmail", key = "#user.email"),
            @CacheEvict(value = "userHasRole", key = "#role.id + '_' + #user.id"),
            @CacheEvict(value = "loadUserByUsername", key = "#user.username")
    })
    public void grantRole(Role role, User user) {
        user.getRoles().add(role);
        userRepository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "userById", key = "#user.id"),
            @CacheEvict(value = "userByUsername", key = "#user.username"),
            @CacheEvict(value = "userByEmail", key = "#user.email"),
            @CacheEvict(value = "userHasRole", key = "#role.id + '_' + #user.id"),
            @CacheEvict(value = "loadUserByUsername", key = "#user.username")
    })
    public void takeRole(Role role, User user) {
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Cacheable(value = "userHasRole", key = "#role.id + '_' + user.id")
    public boolean hasRole(Role role, User user) {
        return user.getRoles().contains(role);
    }

    @Cacheable(value = "defaultRoles")
    public Set<Role> defaultRoles() {
        return roleService.findByRole("ROLE_USER")
                .stream()
                .collect(Collectors.toSet());
    }
}
