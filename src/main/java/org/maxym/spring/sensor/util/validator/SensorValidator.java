package org.maxym.spring.sensor.util.validator;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.SensorRequestDto;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import static java.util.Objects.nonNull;

@Component
@RequiredArgsConstructor
public class SensorValidator implements Validator {

    private final SensorService sensorService;

    @Override
    public boolean supports(@NonNull Class<?> clazz) {
        return SensorRequestDto.class.equals(clazz);
    }

    @Override
    public void validate(@NonNull Object target,
                         @NonNull Errors errors) {

        SensorRequestDto sensorRequestDto = (SensorRequestDto) target;
        String name = sensorRequestDto.name();

        Sensor sensor = sensorService.findByNameNullable(name);

        if (nonNull(sensor)) {
            errors.rejectValue("name", "sensor.exist", "This sensor is already exist.");
        }
    }
}
