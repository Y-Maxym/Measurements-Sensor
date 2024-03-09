package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.*;
import org.maxym.spring.sensor.dto.MeasurementRequest;
import org.maxym.spring.sensor.dto.MeasurementResponse;
import org.maxym.spring.sensor.model.Measurement;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = SensorMapper.class)
public abstract class MeasurementMapper {

    private SensorService sensorService;

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "measurementDate", ignore = true)
    @Mapping(target = "sensor", source = "sensor.name", qualifiedByName = "mappingSensor")
    @Mapping(target = "updatedAt", ignore = true)
    public abstract Measurement map(MeasurementRequest measurementRequest);

    @InheritInverseConfiguration
    @Mapping(target = "sensor", source = "sensor.name")
    public abstract MeasurementResponse map(Measurement measurement);

    @Named("mappingSensor")
    protected Sensor mappingSensor(String name) {
        return sensorService.findByName(name).orElseThrow();
    }

    @Autowired
    public void setSensorService(SensorService sensorService) {
        this.sensorService = sensorService;
    }
}
