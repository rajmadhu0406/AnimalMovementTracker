package com.raj.AnimalMovements.controller;

import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/*
 * The HomeController class defines a simple controller that returns a simple message as a JSON response.
 * This is used for testing
 */
@RestController
@RequestMapping("/api/public")
public class HomeController {
    
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Hello World");
    }

}
