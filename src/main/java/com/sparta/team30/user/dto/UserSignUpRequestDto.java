package com.sparta.team30.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    @NotBlank
    String username;
    @NotBlank
    String email;
    @NotBlank
    String password;
    @NotBlank
    String nickname;
}
