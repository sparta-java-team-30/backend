package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInfoResponse {
    @Schema(description = "회원 아이디",defaultValue = "user1")
    private final String username;
    @Schema(description = "회원 이메일",defaultValue = "email1@email.com")
    private final String email;
    @Schema(description = "회원 닉네임",defaultValue = "myuser")
    private final String nickname;
    @Schema(description = "정보 공개 여부",defaultValue = "true")
    private final boolean isPublic;
    @Schema(description = "회원 가입일",defaultValue = "2025-02-26T00:00:00")
    private final LocalDateTime createdAt;

    @Builder
    public UserInfoResponse(String username, String email, String nickname, boolean isPublic, LocalDateTime createdAt) {
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.isPublic = isPublic;
        this.createdAt = createdAt;
    }
}
