package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserInfoDto;
import org.maxym.spring.sensor.dto.UserResponseDto;
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
        List<UserResponseDto> userResponseDtoList = userMapper.mapList(users);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {

        User user = userService.findById(id);
        UserResponseDto userResponseDto = userMapper.map(user);

        return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
    }

    @GetMapping("/info")
    public ResponseEntity<?> getInfo() {

        User user = userService.currentUser();
        UserInfoDto userInfoDto = userInfoMapper.map(user);

        return ResponseEntity.status(HttpStatus.OK).body(userInfoDto);
    }
}
