package com.example.lab10;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    // Zadanie 1
    @GetMapping()
    public String helloWorld() {
        return "Hello World";
    }
}
