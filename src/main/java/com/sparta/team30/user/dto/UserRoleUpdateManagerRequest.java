package com.sparta.team30.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRoleUpdateManagerRequest {
    @NotBlank(message = "SecretCode는 필수입니다.")
    private String secretCode;
}
