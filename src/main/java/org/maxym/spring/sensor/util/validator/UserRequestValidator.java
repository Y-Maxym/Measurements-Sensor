package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.UserRequest;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.service.UserService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.util.Objects.nonNull;

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

        String username = dto.username();
        String email = dto.email();

        User byUsername = userService.findByUsernameNullable(username);
        User byEmail = userService.findByEmailNullable(email);

        if (nonNull(byUsername)) {
            errors.rejectValue("username", "user.username.exist", "This username is already taken.");
        }
        if (nonNull(byEmail)) {
            errors.rejectValue("email", "user.email.exist", "This email is already taken.");
        }
    }
}
