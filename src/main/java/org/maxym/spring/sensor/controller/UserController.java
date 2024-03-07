package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserInfo;
import org.maxym.spring.sensor.dto.UserResponse;
import org.maxym.spring.sensor.exception.UserNotFoundException;
import org.maxym.spring.sensor.security.model.AuthDetails;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserInfoMapper;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserInfoMapper userInfoMapper;

    @GetMapping
    public ResponseEntity<?> getUsers() {

        List<UserResponse> userResponses = userService.findAll()
                .stream()
                .map(userMapper::map)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        // TODO: maybe in service
        UserResponse user = userService.findById(id)
                .map(userMapper::map)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        AuthDetails details = (AuthDetails) authentication.getPrincipal();

        User user = details.user();

        UserInfo userInfo = userInfoMapper.map(user);

        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }
}
