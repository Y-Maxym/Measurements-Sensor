package org.maxym.spring.sensor.service;

import org.maxym.spring.sensor.error.FieldErrorResponse;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class BindingResultService {

    public void handle(BindingResult bindingResult, Function<List<FieldErrorResponse>, RuntimeException> exception) {

        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw exception.apply(errors);
        }
    }
}
