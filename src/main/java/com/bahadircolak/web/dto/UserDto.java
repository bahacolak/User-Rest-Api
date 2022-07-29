package com.bahadircolak.web.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
