package com.sparta.team30.user.service;

import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.user.dto.*;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void signup(UserSignUpRequestDto userSignupRequestDto);
    void userInfoUpdate(UserDetails userDetails, UserInfoUpdateRequestDto userInfoUpdateRequestDto);
    void deleteUser(UserDetails userDetails, UserDeleteRequestDto userDeleteRequestDto);
    void updateUserToOwner(UserDetails userDetails, UserRoleUpdateOwnerRequest request);
    UserInfoResponse getUserInfo(UserDetails userDetails);
    void updateUserToManager(UserDetailsImpl userDetails, UserRoleUpdateManagerRequest request);
    void updateUserToMaster(UserDetailsImpl userDetails, UserRoleUpdateMasterRequest request);
}
