package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.maxym.spring.sensor.dto.SensorDTO;
import org.maxym.spring.sensor.model.Sensor;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Sensor sensorDTOToSensor(SensorDTO sensorDTO);

    SensorDTO sensorToSensorDTO(Sensor sensor);
}
