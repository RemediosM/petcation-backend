package com.pri.petcationbackend;

import com.pri.petcationbackend.service.RoleService;
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
        return (args) -> {
            roleService.saveRoleIfNotExists("ROLE_ADMIN");
            roleService.saveRoleIfNotExists("ROLE_USER");
            roleService.saveRoleIfNotExists("ROLE_HOTEL");
        };
    }
}
