package org.maxym.spring.sensor.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.LoginRequest;
import org.maxym.spring.sensor.dto.UserRequestDTO;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.security.service.JWTService;
import org.maxym.spring.sensor.service.UserService;
import org.maxym.spring.sensor.util.mapper.UserMapper;
import org.maxym.spring.sensor.util.request.validator.UserRequestValidator;
import org.maxym.spring.sensor.util.responce.error.ErrorResponse;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.UserCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
public class AuthController {

    private final UserService userService;
    private final JWTService JWTService;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;
    private final UserRequestValidator userRequestValidator;

    @PostMapping("/signup")
    public ResponseEntity<Void> createUser(@RequestBody @Validated UserRequestDTO userRequestDTO,
                                                      BindingResult bindingResult,
                                                      HttpServletResponse response) {

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

        String token = JWTService.generateToken(userRequestDTO.getUsername());
        response.setHeader("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest,
                                        HttpServletResponse response) {

        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        String token = JWTService.generateToken(username);
        response.setHeader("Authorization", "Bearer " + token);

        return ResponseEntity.status(HttpStatus.OK).body("User authenticated successfully");

    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(UserCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<String> handleException(AuthenticationException ignore) {
        return new ResponseEntity<>("Bad credentials", HttpStatus.BAD_REQUEST);
    }


}
