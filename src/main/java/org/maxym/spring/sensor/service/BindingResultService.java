package org.maxym.spring.sensor.service;

import org.maxym.spring.sensor.error.FieldErrorEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@Service
public class BindingResultService {

    public void handle(BindingResult bindingResult, Function<List<FieldErrorEntity>, RuntimeException> exception) {

        if (bindingResult.hasErrors()) {
            List<FieldErrorEntity> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorEntity::new)
                    .forEach(errors::add);

            throw exception.apply(errors);
        }
    }
}
