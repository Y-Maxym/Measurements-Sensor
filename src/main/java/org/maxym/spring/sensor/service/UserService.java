package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.model.enums.Authorities;
import org.maxym.spring.sensor.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
        user.setAuthorities(defaultAuthorities());
        userRepository.save(user);
    }

    private Set<Authorities> defaultAuthorities() {
        return new HashSet<>() {{
            add(Authorities.CREATE_SENSOR);
        }};
    }

}
