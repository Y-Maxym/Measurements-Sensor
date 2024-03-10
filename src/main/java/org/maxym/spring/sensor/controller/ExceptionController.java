package org.maxym.spring.sensor.controller;

import org.maxym.spring.sensor.exception.SimpleApplicationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ExceptionController {

    @GetMapping("/catch")
    public void catchError(@RequestAttribute(name = "exception", required = false) SimpleApplicationException exception) {
        if (exception != null) {
            throw exception;
        }
    }
}
