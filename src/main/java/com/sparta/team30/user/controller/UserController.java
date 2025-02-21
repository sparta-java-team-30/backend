package com.sparta.team30.user.controller;

import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.user.dto.*;
import com.sparta.team30.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "UserController", description = "회원 API")
@Slf4j
public class UserController {
    private final UserService userService;

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

    @Operation(summary = "user info", description = "회원 정보 조회 API")
    @ApiResponse(responseCode = "200", description = "정보 조회 성공")
    @ApiResponse(responseCode = "401", description = "올바른 회원 정보가 아님")
    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails){
        UserInfoResponse response = userService.getUserInfo(userDetails);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PatchMapping
    public ResponseEntity<Void> updateUserInfo (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                @RequestBody @Valid UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        userService.userInfoUpdate(userDetails, userInfoUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                            @RequestBody UserDeleteRequestDto userDeleteRequestDto) {
        userService.deleteUser(userDetails, userDeleteRequestDto);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PatchMapping("/roles/owner")
    public ResponseEntity<Void> updateUserToOwner (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                 @RequestBody @Valid UserRoleUpdateOwnerRequest request) {
        userService.updateUserToOwner(userDetails, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/roles/manager")
    public ResponseEntity<Void> updateUserToMaster (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestBody @Valid UserRoleUpdateManagerRequest request) {
        userService.updateUserToManager(userDetails, request);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/roles/master")
    public ResponseEntity<Void> updateUserToManager (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @RequestBody @Valid UserRoleUpdateMasterRequest request) {
        userService.updateUserToMaster(userDetails, request);
        return ResponseEntity.ok().build();
    }
}
