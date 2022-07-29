package com.bahadircolak.service;

import com.bahadircolak.config.PasswordEncoderService;
import com.bahadircolak.model.User;
import com.bahadircolak.repository.UserRepository;
import com.bahadircolak.web.dto.UserDto;
import com.bahadircolak.web.request.RegisterUserRequest;
import com.bahadircolak.web.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoderService passwordEncoderService;

    @Override
    public UserDto createUser(RegisterUserRequest request) {
        if (doesUserExist(request.getEmail())) {
            throw new RuntimeException(String.format("User with email: %s already exists!", request.getEmail()));
        }

        String passwordSalt = passwordEncoderService.generateSalt();
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoderService.hash(request.getPassword(), passwordSalt))
                .salt(passwordSalt)
                .build();

        return userRepository.save(user).toDto();
    }

    private boolean doesUserExist(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDto retrieveUserById(String userId) {
        return userRepository.findById(Long.valueOf(userId))
                .map(User::toDto)
                .orElseThrow(() -> new RuntimeException(String.format("User not found with id: %s", userId)));
    }

    @Override
    public List<UserDto> retrieveAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(User::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updateUser(String userId, UpdateUserRequest request) {
        userRepository.findById(Long.valueOf(userId)).ifPresentOrElse(user -> {
            updateUserInstance(request, user);
            userRepository.save(user);
        }, () -> { throw new RuntimeException(String.format("User not found with id: %s", userId)); });
    }

    private void updateUserInstance(UpdateUserRequest newUser, User oldUser) {
        String newFirstName = newUser.getFirstName();
        String newLastName = newUser.getLastName();
        String newEmail = newUser.getEmail();

        oldUser.setFirstName(Objects.nonNull(newFirstName) ? newFirstName : oldUser.getFirstName());
        oldUser.setLastName(Objects.nonNull(newLastName) ? newLastName : oldUser.getLastName());
        oldUser.setEmail(Objects.nonNull(newEmail) ? newEmail : oldUser.getEmail());
    }

    @Override
    public void deleteUser(String userId) {
        userRepository.findById(Long.valueOf(userId))
                .ifPresentOrElse(
                        userRepository::delete,
                        () -> { throw new RuntimeException(String.format("User not found with id: %s", userId)); }
                );
    }
}
