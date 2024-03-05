package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.RoleResponse;
import org.maxym.spring.sensor.dto.UserRole;
import org.maxym.spring.sensor.exception.RoleNotFoundException;
import org.maxym.spring.sensor.exception.UserNotFoundException;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.RoleService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.RoleMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roles")
@RequiredArgsConstructor
public class RoleController {

    private final UserService userService;
    private final RoleService roleService;
    private final RoleMapper roleMapper;

    @GetMapping
    public ResponseEntity<?> getAllRoles() {
        List<RoleResponse> roles = roleService.findAll().stream()
                .map(roleMapper::map)
                .toList();

        return ResponseEntity.status(HttpStatus.OK).body(roles);
    }

    @PostMapping("/grant")
    public ResponseEntity<?> grantRole(@RequestBody UserRole userRole) {

        User user = userService.findByUsername(userRole.username())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", userRole.username())));

        Role role = roleService.findByRole(userRole.role())
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s not found", userRole.role())));

        userService.grantRole(role, user);

        return ResponseEntity.status(HttpStatus.OK).body("Role was granted");
    }

    @PostMapping("/take")
    public ResponseEntity<?> takeRole(@RequestBody UserRole userRole) {

        User user = userService.findByUsername(userRole.username())
                .orElseThrow(() -> new UserNotFoundException(String.format("User %s not found", userRole.username())));

        Role role = roleService.findByRole(userRole.role())
                .orElseThrow(() -> new RoleNotFoundException(String.format("Role %s not found", userRole.role())));

        if (!userService.hasRole(role, user)) {
            throw new RoleNotFoundException(String.format("User %s does not have %s role", userRole.username(), userRole.role()));
        }

        userService.takeRole(role, user);

        return ResponseEntity.status(HttpStatus.OK).body("Role was taken");
    }
}
