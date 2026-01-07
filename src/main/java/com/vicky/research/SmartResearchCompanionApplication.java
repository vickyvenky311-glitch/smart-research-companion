package com.vicky.research;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SmartResearchCompanionApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartResearchCompanionApplication.class, args);
    }
}
