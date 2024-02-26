package org.maxym.spring.sensor.util.request.validator;

import org.maxym.spring.sensor.dto.SensorDTO;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.SensorAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;

@Component
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public SensorValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return SensorDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target,
                         @NonNull Errors errors) {

        SensorDTO sensor = (SensorDTO) target;

        if (sensorService.findByName(sensor.getName()).isPresent()) {
            errors.rejectValue("name", "sensor.exist", "This sensor is already exist.");
            throw new SensorAlreadyExistException("An error occurred.", Collections.singletonList(new FieldErrorResponse("name", "This sensor is already exist.")));
        }
    }
}
