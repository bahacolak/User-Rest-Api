package com.bahadircolak.model;

import com.bahadircolak.web.dto.UserDto;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String salt;

    public UserDto toDto() {
        return UserDto.builder()
                .userId(id)
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .password(password)
                .build();
    }
}
