package com.sparta.team30.user.dto;

import com.sparta.team30.user.domain.User;
import lombok.Getter;

@Getter
public class UserDto {
    private Long id;

    public UserDto(User user) {
        this.id = user.getId();
    }

}
