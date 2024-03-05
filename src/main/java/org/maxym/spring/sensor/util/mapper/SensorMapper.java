package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.SensorRequestDTO;
import org.maxym.spring.sensor.dto.SensorResponseDTO;
import org.maxym.spring.sensor.model.Sensor;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Sensor map(SensorRequestDTO sensorRequestDTO);

    @InheritConfiguration
    SensorResponseDTO map(Sensor sensor);
}
