package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.model.Sensor;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(source = "name", target = "name")
    Sensor sensorRequestToSensor(SensorRequest sensorRequest);
}
