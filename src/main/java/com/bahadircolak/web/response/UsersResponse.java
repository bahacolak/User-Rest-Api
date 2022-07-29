package com.bahadircolak.web.response;

import com.bahadircolak.web.dto.UserDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsersResponse {

    private List<UserDto> users;
}
