package com.bahadircolak.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class RegisterUserRequest {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
