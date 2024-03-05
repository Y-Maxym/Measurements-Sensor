package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.dto.SensorResponse;
import org.maxym.spring.sensor.model.Sensor;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Sensor map(SensorRequest sensorRequest);

    @InheritConfiguration
    SensorResponse map(Sensor sensor);
}
