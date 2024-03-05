package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.SensorRequestDTO;
import org.maxym.spring.sensor.dto.SensorResponseDTO;
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
        List<SensorResponseDTO> sensorResponseDTOS = sensors.stream()
                .map(sensorMapper::map)
                .collect(Collectors.toList());

        return new ResponseEntity<>(sensorResponseDTOS, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<?> registrationNewSensor(@RequestBody @Valid SensorRequestDTO sensorRequestDTO,
                                                      BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new SensorCreationException("An error occurred.", errors);
        }

        sensorValidator.validate(sensorRequestDTO, bindingResult);

        sensorService.save(sensorMapper.map(sensorRequestDTO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
