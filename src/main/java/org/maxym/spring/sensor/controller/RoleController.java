package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.RoleResponseDto;
import org.maxym.spring.sensor.dto.UserRoleDto;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.RoleService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.RoleMapper;
import org.maxym.spring.sensor.util.validator.UserValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {

        List<Role> roles = roleService.findAll();
        List<RoleResponseDto> roleResponseDtoList = roleMapper.mapList(roles);

        return ResponseEntity.status(HttpStatus.OK).body(roleResponseDtoList);
    }

    @PostMapping("/grant")
    public ResponseEntity<?> grantRole(@RequestBody UserRoleDto userRoleDto) {

        String username = userRoleDto.username();
        String roleName = userRoleDto.role();

        User user = userService.findByUsername(username);
        Role role = roleService.findByRole(roleName);

        userValidator.hasNoRole(user, role);

        userService.grantRole(role, user);

        return ResponseEntity.status(HttpStatus.OK).body("Role was granted");
    }

    @PostMapping("/take")
    public ResponseEntity<?> takeRole(@RequestBody UserRoleDto userRoleDto) {

        String username = userRoleDto.username();
        String roleName = userRoleDto.role();

        User user = userService.findByUsernameWithRole(username);
        Role role = roleService.findByRole(roleName);

        userValidator.hasRole(user, role);

        userService.takeRole(role, user);

        return ResponseEntity.status(HttpStatus.OK).body("Role was taken");
    }
}
