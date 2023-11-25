package com.pri.petcationbackend.service;

import com.pri.petcationbackend.model.User;
import com.pri.petcationbackend.web.dto.ModifyUserDto;
import com.pri.petcationbackend.web.dto.SignUpDto;
import com.pri.petcationbackend.web.dto.UserDto;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface UserService {

    void registerNewUserAccount(SignUpDto signUpDto);
    User findByEmail(String email);
    UserDto loadUserByUsername(String email) throws UsernameNotFoundException;
    User getCurrentUser();

    void changePassword(User user, String newPassword);
    UserDto modifyUser(ModifyUserDto modifyUserDto);
}
