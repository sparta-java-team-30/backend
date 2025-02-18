package com.sparta.team30.user.controller;

import com.sparta.team30.user.dto.UserSignInRequestDto;
import com.sparta.team30.user.dto.UserSignUpRequestDto;
import com.sparta.team30.user.service.UserServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "회원 API")
public class UserController {
    private final UserServiceImpl userService;

    @Operation(summary = "user signup", description = "회원 가입 API")
    @ApiResponse(responseCode = "201", description = "회원 가입 성공 응답 없음")
    @ApiResponse(responseCode = "400", description = "username, email, password 조건에 맞지 않음")
    @ApiResponse(responseCode = "409", description = "이미 사용중인 username or email")
    @PostMapping("/signup")
    public ResponseEntity<Void> signup(@RequestBody @Valid UserSignUpRequestDto userSignupRequestDto) {
        userService.signup(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "user signin", description = "회원 로그인 API")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    @ApiResponse(responseCode = "401", description = "올바른 회원 정보가 아님")
    @PostMapping("/signin")
    public ResponseEntity<Void> signin(@RequestBody @Valid UserSignInRequestDto userSignInRequestDto) {
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
