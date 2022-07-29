package com.bahadircolak.web;

import com.bahadircolak.service.UserService;
import com.bahadircolak.web.request.RegisterUserRequest;
import com.bahadircolak.web.request.UpdateUserRequest;
import com.bahadircolak.web.response.UserResponse;
import com.bahadircolak.web.response.UsersResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users/{userId}")
    public UserResponse retrieveUser(@PathVariable String userId) {
        return UserResponse.builder()
                .user(userService.retrieveUserById(userId))
                .build();
    }

    @GetMapping("/users")
    public UsersResponse retrieveAllUsers() {
        return UsersResponse.builder()
                .users(userService.retrieveAllUsers())
                .build();
    }

    @PostMapping("/users")
    public UserResponse createUser(@RequestBody RegisterUserRequest registerUserRequest) {
        return UserResponse.builder()
                .user(userService.createUser(registerUserRequest))
                .build();
    }

    @DeleteMapping("/users/{userId}")
    public void deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
    }

    @PatchMapping("/users/{userId}")
    public void updateUser(@PathVariable String userId, @RequestBody UpdateUserRequest request) {
        userService.updateUser(userId, request);
    }
}
