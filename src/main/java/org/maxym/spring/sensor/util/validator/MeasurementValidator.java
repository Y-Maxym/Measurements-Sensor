package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.MeasurementRequest;
import org.maxym.spring.sensor.service.SensorService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class MeasurementValidator implements Validator {

    private final SensorService sensorService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MeasurementRequest.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MeasurementRequest measurementRequest = (MeasurementRequest) target;

        if (sensorService.findByName(measurementRequest.sensor().name()).isEmpty()) {
            errors.rejectValue("sensor", "sensor.exist", "This sensor not exist.");
        }
    }
}
