package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.RoleFoundException;
import org.maxym.spring.sensor.exception.RoleNotFoundException;
import org.maxym.spring.sensor.model.Role;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.UserService;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class UserValidator {

    private final UserService userService;

    public void hasRole(User user, Role role) {
        if (!userService.hasRole(user, role)) {
            throw new RoleNotFoundException(format("User %s not have %s.", user.getUsername(), role.getRole()));
        }
    }

    public void hasNoRole(User user, Role role) {
        if (userService.hasRole(user, role)) {
            throw new RoleFoundException(format("User %s have %s.", user.getUsername(), role.getRole()));
        }
    }
}
