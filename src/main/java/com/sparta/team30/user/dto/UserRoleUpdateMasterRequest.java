package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserRoleUpdateMasterRequest {
    @NotBlank(message = "SecretCode는 필수입니다.")
    @Schema(description = "MASTER 승인을 위한 Secret Code", defaultValue = "MASTER_SECRET_CODE")
    private String secretCode;
}
