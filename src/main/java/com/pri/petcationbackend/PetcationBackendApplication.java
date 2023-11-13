package com.pri.petcationbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
public class PetcationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetcationBackendApplication.class, args);
    }

}
