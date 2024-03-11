package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Measurement;
import org.maxym.spring.sensor.repository.MeasurementRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeasurementService {

    private final MeasurementRepository measurementRepository;

    @Cacheable("allMeasurement")
    public List<Measurement> findAll() {
        return measurementRepository.findAll();
    }

    @Cacheable("allRainingDay")
    public Long countAllByRainingTrue() {
        return measurementRepository.countAllByRainingTrue();
    }

    @Caching(evict = {
            @CacheEvict(value = "allMeasurement", allEntries = true),
            @CacheEvict(value = "allRainingDay", allEntries = true)
    })
    @Transactional
    @SuppressWarnings("all")
    public Measurement save(Measurement measurement) {
        return measurementRepository.save(measurement);
    }
}
