package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import org.maxym.spring.sensor.dto.MeasurementDTO;
import org.maxym.spring.sensor.service.MeasurementService;
import org.maxym.spring.sensor.util.mapper.MeasurementMapper;
import org.maxym.spring.sensor.util.request.validator.MeasurementValidator;
import org.maxym.spring.sensor.util.responce.error.ErrorResponse;
import org.maxym.spring.sensor.util.responce.error.FieldErrorResponse;
import org.maxym.spring.sensor.util.responce.exception.MeasurementCreationException;
import org.maxym.spring.sensor.util.responce.exception.SensorNotExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;
    private final MeasurementValidator measurementValidator;

    @Autowired
    public MeasurementController(MeasurementService measurementService, MeasurementMapper measurementMapper, MeasurementValidator measurementValidator) {
        this.measurementService = measurementService;
        this.measurementMapper = measurementMapper;
        this.measurementValidator = measurementValidator;
    }

    @GetMapping
    public ResponseEntity<List<MeasurementDTO>> getAllMeasurements() {
        List<MeasurementDTO> measurementDTOS = measurementService.findAll().stream()
                .map(measurementMapper::measurementToMeasurementDTO)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(measurementDTOS);
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<Long> getCountRainyDays() {
        Long rainyDays = measurementService.countAllByRainingTrue();
        return ResponseEntity.status(HttpStatus.OK).body(rainyDays);
    }

    @PostMapping("/add")
    public ResponseEntity<Void> addMeasurement(@RequestBody @Valid MeasurementDTO measurementDTO,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new MeasurementCreationException("An error occurred.", errors);
        }

        measurementValidator.validate(measurementDTO, bindingResult);

        measurementService.save(measurementMapper.measurementDTOToMeasurement(measurementDTO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(SensorNotExistException exception) {
        ErrorResponse response = new ErrorResponse("An error occurred.", exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MeasurementCreationException exception) {
        ErrorResponse response = new ErrorResponse(exception.getMessage(), exception.getErrors(), System.currentTimeMillis());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
