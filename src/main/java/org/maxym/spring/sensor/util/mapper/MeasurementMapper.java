package org.maxym.spring.sensor.util.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.maxym.spring.sensor.dto.MeasurementDTO;
import org.maxym.spring.sensor.model.Measurement;
import org.maxym.spring.sensor.model.Sensor;
import org.maxym.spring.sensor.service.SensorService;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class MeasurementMapper {

    private SensorService sensorService;

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "value", target = "value")
    @Mapping(source = "raining", target = "raining")
    @Mapping(source = "sensor.name", target = "sensor", qualifiedByName = "mappingSensor")
    @Mapping(target = "measurementDate", ignore = true)
    abstract public Measurement measurementDTOToMeasurement(MeasurementDTO measurementDTO);

    @Mapping(source = "value", target = "value")
    @Mapping(source = "raining", target = "raining")
    @Mapping(source = "sensor", target = "sensor")
    abstract public MeasurementDTO measurementToMeasurementDTO(Measurement measurement);

    @Named("mappingSensor")
    protected Sensor mappingSensor(String name) {
        return sensorService.findByName(name).orElseThrow();
    }

    @Autowired
    public void setSensorService(SensorService sensorService) {
        this.sensorService = sensorService;
    }
}
