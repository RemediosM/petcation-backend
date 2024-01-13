package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.RoleRepository;
import com.pri.petcationbackend.model.Role;
import com.pri.petcationbackend.web.dto.RoleEnum;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public void saveRolesIfNotExists(RoleEnum[] roleNames) {
        List<Role> roles = new ArrayList<>();
        for (RoleEnum role : roleNames) {
            if (roleRepository.findByName(role.name()).isEmpty()) {
                roles.add(new Role(role.name()));
            }
        }
        roleRepository.saveAll(roles);
    }

    @Override
    public void saveRoleIfNotExists(String roleName) {
        if (roleRepository.findByName(roleName).isEmpty()) {
                roleRepository.save(new Role(roleName));
        }
    }
}
