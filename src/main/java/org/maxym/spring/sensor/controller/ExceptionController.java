package org.maxym.spring.sensor.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.maxym.spring.sensor.exception.SimpleApplicationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/error")
public class ExceptionController {

    @GetMapping("/catch")
    public void catchError(HttpServletRequest request) {
        Object exception = request.getAttribute("exception");
        if (exception instanceof SimpleApplicationException e) {
            throw e;
        }
    }
}
