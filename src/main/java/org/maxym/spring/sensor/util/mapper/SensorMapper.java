package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.*;
import org.maxym.spring.sensor.dto.SensorRequest;
import org.maxym.spring.sensor.dto.SensorResponse;
import org.maxym.spring.sensor.model.Sensor;

import java.util.List;

@Mapper(componentModel = "spring")
public interface SensorMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    Sensor map(SensorRequest sensorRequest);

    @Named("map")
    @InheritInverseConfiguration
    @Mapping(target = "createdBy", source = "createdBy.username")
    SensorResponse map(Sensor sensor);

    @IterableMapping(qualifiedByName = "map")
    List<SensorResponse> mapList(List<Sensor> sensors);
}
