package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.SensorRequestDto;
import org.maxym.spring.sensor.dto.SensorResponseDto;
import org.maxym.spring.sensor.exception.SensorCreationException;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.mapper.SensorMapper;
import org.maxym.spring.sensor.util.validator.SensorValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final SensorMapper sensorMapper;
    private final BindingResultService bindingResultService;

    @GetMapping
    public ResponseEntity<?> getAllSensors() {

        List<Sensor> sensors = sensorService.findAll();
        List<SensorResponseDto> sensorResponseDtoList = sensorMapper.mapList(sensors);

        return ResponseEntity.status(HttpStatus.OK).body(sensorResponseDtoList);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrationNewSensor(@RequestBody @Validated SensorRequestDto sensorRequestDto,
                                                   BindingResult bindingResult) {

        sensorValidator.validate(sensorRequestDto, bindingResult);
        bindingResultService.handle(bindingResult, SensorCreationException::new);

        sensorService.save(sensorMapper.map(sensorRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
