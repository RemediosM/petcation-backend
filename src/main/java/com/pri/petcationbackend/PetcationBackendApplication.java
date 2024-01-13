package com.pri.petcationbackend;

import com.pri.petcationbackend.service.RoleService;
import com.pri.petcationbackend.web.dto.RoleEnum;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class PetcationBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PetcationBackendApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(RoleService roleService) {
        return args -> roleService.saveRolesIfNotExists(RoleEnum.values());
    }
}
