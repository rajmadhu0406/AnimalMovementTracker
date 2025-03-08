package com.raj.AnimalMovements.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/public")
public class HomeController {
    
    @GetMapping("/")
    public Map<String, String> home() {
        return Map.of("message", "Hello World");
    }

}
