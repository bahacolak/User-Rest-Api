package com.bahadircolak.service;

import com.bahadircolak.web.dto.UserDto;
import com.bahadircolak.web.request.RegisterUserRequest;
import com.bahadircolak.web.request.UpdateUserRequest;

import java.util.List;

public interface UserService {
    UserDto createUser(RegisterUserRequest registrationDto);

    UserDto retrieveUserById(String userId);

    List<UserDto> retrieveAllUsers();

    void updateUser(String userId, UpdateUserRequest request);

    void deleteUser(String userId);
}
