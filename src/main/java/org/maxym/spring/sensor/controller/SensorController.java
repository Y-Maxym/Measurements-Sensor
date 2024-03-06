package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.dto.SensorResponse;
import org.maxym.spring.sensor.exception.SensorCreationException;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.mapper.SensorMapper;
import org.maxym.spring.sensor.util.validator.SensorValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

        // TODO: map list
        List<SensorResponse> sensorResponses = sensorService.findAll()
                .stream()
                .map(sensorMapper::map)
                .collect(Collectors.toList());

        return new ResponseEntity<>(sensorResponses, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrationNewSensor(@RequestBody @Valid SensorRequest sensorRequest,
                                                   BindingResult bindingResult) {

        sensorValidator.validate(sensorRequest, bindingResult);
        bindingResultService.handle(bindingResult, SensorCreationException::new);

        sensorService.save(sensorMapper.map(sensorRequest));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
