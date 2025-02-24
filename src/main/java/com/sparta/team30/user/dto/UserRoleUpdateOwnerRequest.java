package com.sparta.team30.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRoleUpdateOwnerRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "사업자번호는 11자리의 숫자만 가능 합니다.")
    private String businessNumber;
}
