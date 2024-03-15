package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.*;
import org.maxym.spring.sensor.dto.SensorRequestDto;
import org.maxym.spring.sensor.dto.SensorResponseDto;
import org.maxym.spring.sensor.model.Sensor;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Sensor map(SensorRequestDto sensorRequestDto);

    @Named("map")
    @InheritInverseConfiguration
    @Mapping(target = "createdBy", source = "createdBy.username")
    SensorResponseDto map(Sensor sensor);

    @IterableMapping(qualifiedByName = "map")
    List<SensorResponseDto> mapList(List<Sensor> sensors);
}
