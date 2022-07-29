package com.bahadircolak.web.response;

import com.bahadircolak.web.dto.UserDto;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private UserDto user;
}
