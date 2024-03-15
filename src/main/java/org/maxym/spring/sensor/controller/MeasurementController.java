package org.maxym.spring.sensor.controller;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.dto.MeasurementRequestDto;
import org.maxym.spring.sensor.dto.MeasurementResponseDto;
import org.maxym.spring.sensor.exception.MeasurementCreationException;
import org.maxym.spring.sensor.model.Measurement;
import org.maxym.spring.sensor.service.BindingResultService;
import org.maxym.spring.sensor.service.MeasurementService;
import org.maxym.spring.sensor.util.mapper.MeasurementMapper;
import org.maxym.spring.sensor.util.validator.MeasurementRequestDtoValidator;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurements")
@RequiredArgsConstructor
public class MeasurementController {

    private final MeasurementService measurementService;
    private final MeasurementMapper measurementMapper;
    private final MeasurementRequestDtoValidator measurementRequestDtoValidator;
    private final BindingResultService bindingResultService;

    @GetMapping
    public ResponseEntity<?> getAllMeasurements() {

        List<Measurement> measurements = measurementService.findAll();
        List<MeasurementResponseDto> measurementResponseDtoList = measurementMapper.mapList(measurements);

        return ResponseEntity.status(HttpStatus.OK).body(measurementResponseDtoList);
    }

    @GetMapping("/rainyDaysCount")
    public ResponseEntity<?> getCountRainyDays() {

        Long rainyDays = measurementService.countAllByRainingTrue();

        return ResponseEntity.status(HttpStatus.OK).body(rainyDays);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addMeasurement(@RequestBody @Validated MeasurementRequestDto measurementRequestDto,
                                            BindingResult bindingResult) {

        measurementRequestDtoValidator.validate(measurementRequestDto, bindingResult);
        bindingResultService.handle(bindingResult, MeasurementCreationException::new);

        measurementService.save(measurementMapper.map(measurementRequestDto));

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
