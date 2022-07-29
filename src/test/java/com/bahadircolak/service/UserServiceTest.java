package com.bahadircolak.service;

import com.bahadircolak.config.PasswordEncoderService;
import com.bahadircolak.model.User;
import com.bahadircolak.repository.UserRepository;
import com.bahadircolak.web.dto.UserDto;
import com.bahadircolak.web.request.RegisterUserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoderService passwordEncoderService;

    @Test
    void should_create_user_successfully() {
        // given
        RegisterUserRequest request = new RegisterUserRequest("bahadır", "çolak", "baha@colak.com", "password");

        User mockUser = User.builder()
                .firstName("bahadır")
                .lastName("çolak")
                .email("baha@colak.com")
                .password("hashedPassword")
                .build();

        when(userRepository.existsByEmail("baha@colak.com")).thenReturn(Boolean.FALSE);
        when(passwordEncoderService.generateSalt()).thenReturn("salt");
        when(passwordEncoderService.hash("password", "salt")).thenReturn("hashedPassword");
        when(userRepository.save(any())).thenReturn(mockUser);

        // when
        UserDto newUserDto = userService.createUser(request);

        // then
        assertThat(newUserDto.getFirstName()).isEqualTo("bahadır");
        assertThat(newUserDto.getLastName()).isEqualTo("çolak");
        assertThat(newUserDto.getEmail()).isEqualTo("baha@colak.com");
        assertThat(newUserDto.getPassword()).isEqualTo("hashedPassword");
    }

    @Test
    void should_fail_while_creating_user() {
        // given
        RegisterUserRequest request = new RegisterUserRequest("bahadır", "çolak", "baha@colak.com", "password");

        when(userRepository.existsByEmail("baha@colak.com")).thenReturn(Boolean.TRUE);

        // then
        Throwable exception = assertThrows(RuntimeException.class, () -> userService.createUser(request));
        assertEquals("User with email: baha@colak.com already exists!", exception.getMessage());
    }
}