package com.pri.petcationbackend.service;

import com.pri.petcationbackend.dao.RoleRepository;
import com.pri.petcationbackend.model.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void saveRoleIfNotExists_whenRoleFound() {
        // given
        String fakeRole = "fake role";
        given(roleRepository.findByName(fakeRole)).willReturn(Optional.of(new Role()));

        // when
        roleService.saveRoleIfNotExists(fakeRole);

        // then
        verify(roleRepository, times(1)).findByName(fakeRole);
        verifyNoMoreInteractions(roleRepository);
    }

    @Test
    void saveRoleIfNotExists_shouldSaveRole() {
        // given
        String fakeRole = "fake role";
        given(roleRepository.findByName(fakeRole)).willReturn(Optional.empty());

        // when
        roleService.saveRoleIfNotExists(fakeRole);

        // then
        verify(roleRepository, times(1)).findByName(fakeRole);
        verify(roleRepository, times(1)).save(any(Role.class));
    }

}