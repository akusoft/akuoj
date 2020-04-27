package com.howie.akuoj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AkuojApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkuojApplication.class, args);
    }

    @GetMapping("/")
    public String hello(@RequestParam(value = "name", defaultValue = "AKUOJ!") String name) {
        return String.format("Hello %s!", name);
    }
}
