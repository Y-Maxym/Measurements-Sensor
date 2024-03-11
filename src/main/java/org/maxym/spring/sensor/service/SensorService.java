package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.exception.SensorNotFoundException;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.SensorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final UserService userService;

    @Cacheable("allSensors")
    public List<Sensor> findAll() {
        return sensorRepository.findAll();
    }

    @Cacheable(value = "sensorByName", key = "#sensorName")
    public Sensor findByName(String sensorName) {
        return sensorRepository.findByName(sensorName)
                .orElseThrow(() -> new SensorNotFoundException(format("Sensor %s not found.", sensorName)));
    }

    @Cacheable(value = "sensorByNameNullable", key = "#sensorName")
    public Sensor findByNameNullable(String sensorName) {
        return sensorRepository.findByName(sensorName)
                .orElse(null);
    }

    @Caching(evict = {
            @CacheEvict(value = "allSensors", allEntries = true),
    }, put = {
            @CachePut(value = "sensorByName", key = "#sensor.name")
    })
    @Transactional
    @SuppressWarnings("all")
    public Sensor save(Sensor sensor) {
        User currentUser = userService.currentUser();
        sensor.setCreatedBy(currentUser);
        return sensorRepository.save(sensor);
    }
}
