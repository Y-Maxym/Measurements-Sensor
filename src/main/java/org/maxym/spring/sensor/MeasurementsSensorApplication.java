package org.maxym.spring.sensor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
//@EnableCaching
public class MeasurementsSensorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MeasurementsSensorApplication.class, args);
    }
}
