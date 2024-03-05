package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.MeasurementRequestDTO;
import org.maxym.spring.sensor.dto.MeasurementResponseDTO;
import org.maxym.spring.sensor.service.MeasurementService;
import org.maxym.spring.sensor.util.mapper.MeasurementMapper;
import org.maxym.spring.sensor.util.validator.MeasurementValidator;
import org.maxym.spring.sensor.error.FieldErrorResponse;
import org.maxym.spring.sensor.exception.MeasurementCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;
    private final MeasurementValidator measurementValidator;

    @GetMapping
    public ResponseEntity<?> getAllMeasurements() {
        List<MeasurementResponseDTO> measurementResponseDTOS = measurementService.findAll().stream()
                .map(measurementMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(measurementResponseDTOS);
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<?> getCountRainyDays() {
        Long rainyDays = measurementService.countAllByRainingTrue();
        return ResponseEntity.status(HttpStatus.OK).body(rainyDays);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementRequestDTO measurementRequestDTO,
                                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            List<FieldErrorResponse> errors = new ArrayList<>();

            bindingResult.getFieldErrors().stream()
                    .map(FieldErrorResponse::new)
                    .forEach(errors::add);

            throw new MeasurementCreationException("An error occurred.", errors);
        }

        measurementValidator.validate(measurementRequestDTO, bindingResult);

        measurementService.save(measurementMapper.map(measurementRequestDTO));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
