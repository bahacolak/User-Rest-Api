package com.bahadircolak.web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserRegistrationDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
