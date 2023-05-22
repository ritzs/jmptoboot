package com.example.jmptoboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JmptobootApplication {

    public static void main(String[] args) {
        SpringApplication.run(JmptobootApplication.class, args);
    }

}
