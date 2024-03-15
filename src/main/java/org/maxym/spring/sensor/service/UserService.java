package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.UserNotFoundException;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public void setPasswordEncoder(@Lazy PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Cacheable("allUsers")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Cacheable(value = "userById", key = "#id")
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(format("User with id %d not found.", id)));
    }

    @Cacheable(value = "userByUsername", key = "#username")
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(format("User %s not found.", username)));
    }

    @Cacheable(value = "userByUsernameNullable", key = "#username")
    public User findByUsernameNullable(String username) {
        return userRepository.findByUsername(username)
                .orElse(null);
    }

    public User findByUsernameWithRole(String username) {
        return userRepository.findByUsernameWithRole(username)
                .orElseThrow(() -> new UserNotFoundException(format("User %s not found.", username)));
    }

    @Cacheable(value = "userByEmail", key = "#email")
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(format("User with email %s not found.", email)));
    }

    @Cacheable(value = "userByEmailNullable", key = "#email")
    public User findByEmailNullable(String email) {
        return userRepository.findByEmail(email)
                .orElse(null);
    }

    public User currentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
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
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(new HashSet<>(defaultRoles()));
        return userRepository.save(user);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "allUsers", allEntries = true),
            @CacheEvict(value = "userById", key = "#user.id"),
            @CacheEvict(value = "userByUsername", key = "#user.username"),
            @CacheEvict(value = "userByEmail", key = "#user.email"),
            @CacheEvict(value = "userHasRole", key = "#role.id + '_' + #user.id"),
            @CacheEvict(value = "loadUserByUsername", key = "#user.username"),
            @CacheEvict(value = "userDetails", key = "#user.username")
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
            @CacheEvict(value = "loadUserByUsername", key = "#user.username"),
            @CacheEvict(value = "userDetails", key = "#user.username")
    })
    public void takeRole(Role role, User user) {
        user.getRoles().remove(role);
        userRepository.save(user);
    }

    @Cacheable(value = "userHasRole", key = "#role.id + '_' + #user.id")
    public boolean hasRole(User user, Role role) {
        return user.getRoles().contains(role);
    }

    public Set<Role> defaultRoles() {
        return Set.of(roleService.findByRole("ROLE_USER"));
    }

    @Override
    @Cacheable(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> byUsername = userRepository.findByUsernameWithRole(username);

        if (byUsername.isEmpty()) {
            throw new UserNotFoundException(format("User %s not found.", username));
        }
        return byUsername.get();
    }
}
