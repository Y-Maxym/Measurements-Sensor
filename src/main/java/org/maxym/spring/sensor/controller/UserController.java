package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserInfo;
import org.maxym.spring.sensor.dto.UserResponse;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserInfoMapper;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

        List<User> users = userService.findAll();
        List<UserResponse> userResponses = userMapper.mapList(users);

        return ResponseEntity.status(HttpStatus.OK).body(userResponses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        User user = userService.findById(id);
        UserResponse userResponse = userMapper.map(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponse);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {

        User user = userService.currentUser();
        UserInfo userInfo = userInfoMapper.map(user);

        return ResponseEntity.status(HttpStatus.OK).body(userInfo);
    }
}
