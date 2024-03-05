package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserRequest;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserRequestValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return UserRequest.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        UserRequest dto = (UserRequest) target;

        Optional<User> byUsername = userService.findByUsername(dto.username());
        Optional<User> byEmail = userService.findByEmail(dto.email());

        if (byUsername.isPresent()) {
            errors.rejectValue("username", "user.username.exist", "This username is already taken.");
        }
        if (byEmail.isPresent()) {
            errors.rejectValue("email", "user.email.exist", "This email is already taken.");
        }
    }
}
