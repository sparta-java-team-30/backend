package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDeleteRequestDto {
    @Schema(description = "회원 탈퇴를 위한 비밀번호", defaultValue = "Password1!")
    private String password;

    @Builder
    public UserDeleteRequestDto(String password) {
        this.password = password;
    }
}
