package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserSignInRequestDto {
    @NotNull
    @Schema(description = "사용자 id", defaultValue = "user1")
    private String username;
    @NotNull
    @Schema(description = "사용자 password", defaultValue = "Password1!")
    private String password;
}
