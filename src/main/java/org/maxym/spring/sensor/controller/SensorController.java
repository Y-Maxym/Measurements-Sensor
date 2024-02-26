package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.service.SensorService;
import org.maxym.spring.sensor.util.mapper.SensorMapper;
import org.maxym.spring.sensor.util.request.validator.SensorValidator;
import org.maxym.spring.sensor.util.responce.SensorErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.SensorAlreadyExistException;
import org.maxym.spring.sensor.util.responce.exception.SensorCreationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/registration")
    public ResponseEntity<Void> registrationNewSensor(@RequestBody @Valid SensorRequest sensorRequest,
                                                               BindingResult bindingResult) {

        sensorValidator.validate(sensorRequest, bindingResult);

        if (bindingResult.hasErrors()) {
            StringBuilder errors = new StringBuilder();

            bindingResult.getFieldErrors()
                    .forEach(field -> errors
                            .append("Field: ")
                            .append(field.getField())
                            .append(" Error: ")
                            .append(field.getDefaultMessage())
                    );

            throw new SensorCreationException(errors.toString());
        }

        sensorService.save(sensorMapper.sensorRequestToSensor(sensorRequest));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorCreationException exception) {
        SensorErrorResponse response = new SensorErrorResponse(exception.getMessage(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<SensorErrorResponse> handleException(SensorAlreadyExistException ignore) {
        SensorErrorResponse response = new SensorErrorResponse("This sensor is already exist.", System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
