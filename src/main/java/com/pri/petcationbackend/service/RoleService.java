package com.pri.petcationbackend.service;

import com.pri.petcationbackend.web.dto.RoleEnum;

public interface RoleService {

    void saveRoleIfNotExists(RoleEnum[] roleName);
}
