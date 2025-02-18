package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserSignUpRequestDto {
    @NotBlank
    @Size(min = 4, max = 10)
    @Pattern(regexp = "[a-z0-9]+$",
            message = "알파벳 소문자와 숫자로만 구성되야 합니다.")
    @Schema(description = "사용자 id", defaultValue = "user1")
    String username;

    @NotBlank
    @Email
    @Schema(description = "사용자 email", defaultValue = "email1@email.com")
    String email;

    @NotBlank
    @Size(min = 8, max = 15)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+=-]).*$",
            message = "알파벳 대소문자, 숫자, 특수문자를 포함해야 합니다.")
    @Schema(description = "사용자 password", defaultValue = "Password1!")
    String password;

    @NotBlank
    @Size(min = 4, max = 20)
    @Schema(description = "사용자 nickname", defaultValue = "myuser")
    String nickname;
}
