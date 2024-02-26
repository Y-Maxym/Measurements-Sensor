package org.maxym.spring.sensor.util.request.validator;

import org.maxym.spring.sensor.dto.MeasurementDTO;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.SensorNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collections;

@Component
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Autowired
    public MeasurementValidator(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MeasurementDTO.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MeasurementDTO measurementDTO = (MeasurementDTO) target;

        if (sensorService.findByName(measurementDTO.getSensor().getName()).isEmpty()) {
            errors.rejectValue("sensor", "sensor.exist", "This sensor not exist.");
            throw new SensorNotExistException("An error occurred.", Collections.singletonList(new FieldErrorResponse("sensor", "This sensor not exist.")));
        }

    }
}
