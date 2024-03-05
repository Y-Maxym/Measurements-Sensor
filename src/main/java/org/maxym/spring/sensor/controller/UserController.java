package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserResponseDTO;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.model.AuthDetails;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public ResponseEntity<?> getUsers() {
        List<UserResponseDTO> userResponseDTOS = userService.findAll().stream()
                .map(userMapper::map)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTOS);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);

        if (optionalUser.isEmpty()) {

            throw new UserNotFoundException("User not found");
        }
        User user = optionalUser.get();
        UserResponseDTO userResponseDTO = userMapper.map(user);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails details = (AuthDetails) authentication.getPrincipal();

        System.out.println(details.user());

        return ResponseEntity.ok().build();
    }
}
