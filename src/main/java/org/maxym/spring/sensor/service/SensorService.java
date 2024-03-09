package org.maxym.spring.sensor.service;

import lombok.RequiredArgsConstructor;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.model.User;
import org.maxym.spring.sensor.repository.SensorRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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

    @Cacheable(value = "sensorByName", key = "#name")
    public Optional<Sensor> findByName(String name) {
        return sensorRepository.findByName(name);
    }

    @Transactional
    @CacheEvict(value = "allSensors", allEntries = true)
    public void save(Sensor sensor) {
        User currentUser = userService.currentUser();
        sensor.setCreatedBy(currentUser);
        sensorRepository.save(sensor);
    }
}
