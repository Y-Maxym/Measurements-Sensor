package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.MeasurementRequestDto;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class MeasurementRequestDtoValidator implements Validator {

    private final SensorService sensorService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return MeasurementRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target, @NonNull Errors errors) {
        MeasurementRequestDto measurementRequestDto = (MeasurementRequestDto) target;

        String name = measurementRequestDto.sensor().name();
        Sensor sensor = sensorService.findByNameNullable(name);

        if (isNull(sensor)) {
            errors.rejectValue("sensor", "sensor.exist", "This sensor not exist.");
        }
    }
}
