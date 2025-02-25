package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRoleUpdateOwnerRequest {
    @NotBlank
    @Pattern(regexp = "^[0-9]{11}$", message = "사업자번호는 11자리의 숫자만 가능 합니다.")
    @Schema(description = "사용자 사업자번호", defaultValue = "12345678901")
    private String businessNumber;
}
