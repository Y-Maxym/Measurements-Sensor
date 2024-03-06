package org.maxym.spring.sensor.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.MeasurementRequest;
import org.maxym.spring.sensor.dto.MeasurementResponse;
import org.maxym.spring.sensor.exception.MeasurementCreationException;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.MeasurementService;
import org.maxym.spring.sensor.util.mapper.MeasurementMapper;
import org.maxym.spring.sensor.util.validator.MeasurementValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;
    private final MeasurementValidator measurementValidator;
    private final BindingResultService bindingResultService;

    @GetMapping
    public ResponseEntity<?> getAllMeasurements() {
        List<MeasurementResponse> measurementResponses = measurementService.findAll().stream()
                .map(measurementMapper::map)
                .toList();
        return ResponseEntity.status(HttpStatus.OK).body(measurementResponses);
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<?> getCountRainyDays() {
        Long rainyDays = measurementService.countAllByRainingTrue();
        return ResponseEntity.status(HttpStatus.OK).body(rainyDays);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Valid MeasurementRequest measurementRequest,
                                            BindingResult bindingResult) {

        measurementValidator.validate(measurementRequest, bindingResult);
        bindingResultService.handle(bindingResult, MeasurementCreationException::new);

        measurementService.save(measurementMapper.map(measurementRequest));

        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
