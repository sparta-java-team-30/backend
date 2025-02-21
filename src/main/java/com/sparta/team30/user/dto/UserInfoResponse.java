package com.sparta.team30.user.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserInfoResponse {
    private final String username;
    private final String email;
    private final String nickname;
    private final boolean isPublic;
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
