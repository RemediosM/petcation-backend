package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.RoleRepository;
import com.pri.petcationbackend.model.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;

    @Override
    public void saveRoleIfNotExists(String roleName) {
        if(roleRepository.findByName(roleName).isEmpty())
            roleRepository.save(new Role(roleName));
    }
}
