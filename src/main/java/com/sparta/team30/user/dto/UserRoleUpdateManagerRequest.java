package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRoleUpdateManagerRequest {
    @NotBlank(message = "SecretCode는 필수입니다.")
    @Schema(description = "MANAGER 승인을 위한 Secret Code", defaultValue = "MANAGER_SECRET_CODE")
    private String secretCode;
}
