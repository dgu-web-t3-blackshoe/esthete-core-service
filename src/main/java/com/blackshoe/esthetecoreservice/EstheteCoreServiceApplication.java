package com.blackshoe.esthetecoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EstheteCoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EstheteCoreServiceApplication.class, args);
    }

}
