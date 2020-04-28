package com.howiezhao.akuoj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class AkuojApplication {

    public static void main(String[] args) {
        SpringApplication.run(AkuojApplication.class, args);
    }
}
