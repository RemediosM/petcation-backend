package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.UserRepository;
import com.pri.petcationbackend.model.Role;
import com.pri.petcationbackend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void loadUserByUsername_whenUserIsNull() {
        // given
        String email = "email";
        given(userRepository.findByEmailIgnoreCase(email)).willReturn(null);

        // when
        Throwable result = catchThrowable(() -> userDetailsService.loadUserByUsername(email));

        // then
        assertTrue(result instanceof UsernameNotFoundException);
        assertEquals("No user found with username: " + email, result.getMessage());
    }

    @Test
    void loadUserByUsername_shouldReturnUserDetails() {
        // given
        String email = "email";
        String password = "pass";
        String roleName = "role";
        Role role = new Role();
        role.setName(roleName);
        User user = User.builder()
                .email(email)
                .password(password)
                .roles(Set.of(role))
                .enabled(true)
                .build();
        given(userRepository.findByEmailIgnoreCase(email)).willReturn(user);

        // when
        UserDetails result = userDetailsService.loadUserByUsername(email);

        // then
        assertEquals(password, result.getPassword());
        assertEquals(email, result.getUsername());
        assertEquals(1, result.getAuthorities().size());
        assertTrue(result.isAccountNonExpired());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isAccountNonLocked());
        assertTrue(result.isEnabled());
    }

}