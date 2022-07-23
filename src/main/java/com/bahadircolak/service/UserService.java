package com.bahadircolak.service;

import com.bahadircolak.model.User;
import com.bahadircolak.web.dto.UserRegistrationDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {
    User save(UserRegistrationDto registrationDto);
}
