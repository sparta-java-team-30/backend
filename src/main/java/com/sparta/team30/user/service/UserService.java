package com.sparta.team30.user.service;

import com.sparta.team30.user.dto.UserDeleteRequestDto;
import com.sparta.team30.user.dto.UserInfoUpdateRequestDto;
import com.sparta.team30.user.dto.UserSignUpRequestDto;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void signup(UserSignUpRequestDto userSignupRequestDto);
    void userInfoUpdate(UserDetails userDetails, UserInfoUpdateRequestDto userInfoUpdateRequestDto);
    void deleteUser(UserDetails userDetails, UserDeleteRequestDto userDeleteRequestDto);
}
