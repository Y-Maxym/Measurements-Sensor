package org.maxym.spring.sensor.security.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.UserNotFoundException;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.UserRepository;
import org.maxym.spring.sensor.security.model.AuthDetails;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    @Cacheable(value = "userDetails", key = "#username")
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> byUsername = userRepository.findByUsernameRoleFetch(username);

        if (byUsername.isEmpty()) {
            throw new UserNotFoundException(format("User %s not found.", username));
        }
        return new AuthDetails(byUsername.get());
    }
}
