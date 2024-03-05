package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.dto.SensorResponse;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.mapper.SensorMapper;
import org.maxym.spring.sensor.util.validator.SensorValidator;
import org.maxym.spring.sensor.error.FieldErrorResponse;
import org.maxym.spring.sensor.exception.SensorCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
@RequiredArgsConstructor
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final SensorMapper sensorMapper;

    @GetMapping
    public ResponseEntity<?> getAllSensors() {
        List<Sensor> sensors = sensorService.findAll();
        List<SensorResponse> sensorResponses = sensors.stream()
                .map(sensorMapper::map)
                .collect(Collectors.toList());

        return new ResponseEntity<>(sensorResponses, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrationNewSensor(@RequestBody @Valid SensorRequest sensorRequest,
                                                      BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new SensorCreationException("An error occurred.", errors);
        }

        sensorValidator.validate(sensorRequest, bindingResult);

        sensorService.save(sensorMapper.map(sensorRequest));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
