package org.maxym.spring.sensor.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.maxym.spring.sensor.dto.LoginRequest;
import org.maxym.spring.sensor.dto.UserRequestDTO;
import org.maxym.spring.sensor.dto.UserResponseDTO;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.security.service.AuthenticationService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.request.validator.UserRequestValidator;
import org.maxym.spring.sensor.util.responce.error.ErrorResponse;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.UserCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;
    private final UserRequestValidator userRequestValidator;
    private final AuthenticationService authenticationService;

    @PostMapping("/signup")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Validated UserRequestDTO userRequestDTO,
                                                      BindingResult bindingResult) {

        userRequestValidator.validate(userRequestDTO, bindingResult);

        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new UserCreationException("An error occurred.", errors);
        }

        User user = userMapper.userRequestDTOToUser(userRequestDTO);
        userService.save(user);
        UserResponseDTO userResponseDTO = userMapper.userToUserResponseDTO(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletRequest request,
                                        HttpServletResponse response) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        try {
            Authentication authenticate = authenticationService.authenticate(username, password);
            authenticationService.saveContext(request, response, authenticate);
        } catch (AuthenticationException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Bad credentials");
        }
        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }


}
