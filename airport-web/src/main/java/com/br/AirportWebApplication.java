package com.br;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.br.mapper")
public class AirportWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(AirportWebApplication.class, args);
    }
}

