package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.RoleEnum;

public interface RoleService {

    void saveRolesIfNotExists(RoleEnum[] roleName);

    void saveRoleIfNotExists(String roleName);
}
