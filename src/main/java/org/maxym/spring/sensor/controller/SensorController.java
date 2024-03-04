package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import org.maxym.spring.sensor.dto.SensorDTO;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.mapper.SensorMapper;
import org.maxym.spring.sensor.util.request.validator.SensorValidator;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.SensorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sensors")
public class SensorController {

    private final SensorService sensorService;
    private final SensorValidator sensorValidator;
    private final SensorMapper sensorMapper;

    @Autowired
    public SensorController(SensorService sensorService, SensorValidator sensorValidator, SensorMapper sensorMapper) {
        this.sensorService = sensorService;
        this.sensorValidator = sensorValidator;
        this.sensorMapper = sensorMapper;
    }

    @GetMapping
    public ResponseEntity<List<SensorDTO>> getAllSensors() {
        List<Sensor> sensors = sensorService.findAll();
        List<SensorDTO> sensorDTOs = sensors.stream()
                .map(sensorMapper::sensorToSensorDTO)
                .collect(Collectors.toList());

        return new ResponseEntity<>(sensorDTOs, HttpStatus.OK);
    }

    @PostMapping("/registration")
    public ResponseEntity<Void> registrationNewSensor(@RequestBody @Valid SensorDTO sensorDTO,
                                                      BindingResult bindingResult) {


        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new SensorCreationException("An error occurred.", errors);
        }

        sensorValidator.validate(sensorDTO, bindingResult);

        sensorService.save(sensorMapper.sensorDTOToSensor(sensorDTO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
