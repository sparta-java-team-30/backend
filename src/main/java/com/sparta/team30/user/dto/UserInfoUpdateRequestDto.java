package com.sparta.team30.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserInfoUpdateRequestDto {
    @Email
    @Schema(description = "변경할 email", defaultValue = "email2@email.com")
    private String email;

    @Size(min = 4, max = 20)
    @Schema(description = "변경할 nickname", defaultValue = "myuser1")
    private String nickname;

    private Boolean isPublic;
}
