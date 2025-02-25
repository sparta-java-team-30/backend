package com.sparta.team30.user.service;

import com.sparta.team30.common.exception.UserEmailAlreadyExistsException;
import com.sparta.team30.common.exception.UserPasswordIncorrectException;
import com.sparta.team30.common.exception.UsernameAlreadyExistsException;
import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.dto.*;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.sparta.team30.user.domain.UserRoleEnum.MANAGER;
import static com.sparta.team30.user.domain.UserRoleEnum.MASTER;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_USER_CREATED_BY = "SYSTEM";
    private static final boolean DEFAULT_IS_PUBLIC = true;
    @Value("${secret.code.master}")
    private String MASTER_SECRET_CODE;
    @Value("${secret.code.manager}")
    private String MANAGER_SECRET_CODE;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    @Override
    public void signup(UserSignUpRequestDto userSignupRequestDto) {
        if (isUsernameDuplicated(userSignupRequestDto.getUsername())) {
            throw new UsernameAlreadyExistsException(userSignupRequestDto.getUsername() + "은 이미 사용중인 아이디 입니다.");
        }

        if (isEmailDuplicated(userSignupRequestDto.getEmail())) {
            throw new UserEmailAlreadyExistsException(userSignupRequestDto.getEmail() + "은 이미 사용중인 이메일 입니다.");
        }

        User user = User.builder()
                .username(userSignupRequestDto.getUsername())
                .email(userSignupRequestDto.getEmail())
                .password(encodePassword(userSignupRequestDto.getPassword()))
                .nickname(userSignupRequestDto.getNickname())
                .isPublic(DEFAULT_IS_PUBLIC)
                .role(UserRoleEnum.USER)
                .build();

        userRepository.save(user);
        userRepository.updateCreatedBy(DEFAULT_USER_CREATED_BY, user.getId());
    }

    @Transactional
    @Override
    public void userInfoUpdate(UserDetails userDetails, UserInfoUpdateRequestDto userInfoUpdateRequestDto) {
        String username = getUsername(userDetails);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));
        if (!user.getIsPublic().equals(userInfoUpdateRequestDto.getIsPublic())) {
            user.updateIsPublic(userInfoUpdateRequestDto.getIsPublic());
        }
        if (userInfoUpdateRequestDto.getEmail() != null) {
            if (isEmailDuplicated(userInfoUpdateRequestDto.getEmail())) {
                throw new UserEmailAlreadyExistsException(userInfoUpdateRequestDto.getEmail() +
                        "은 이미 사용중인 이메일 입니다.");
            }
            user.updateEmail(userInfoUpdateRequestDto.getEmail());
        }
        if (userInfoUpdateRequestDto.getNickname() != null) {
            user.updateNickname(userInfoUpdateRequestDto.getNickname());
        }
    }

    @Transactional
    @Override
    public void deleteUser(UserDetails userDetails, UserDeleteRequestDto userDeleteRequestDto) {
        String username = getUsername(userDetails);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));
        if (!passwordEncoder.matches(userDeleteRequestDto.getPassword(), user.getPassword())) {
            throw new UserPasswordIncorrectException("비밀번호가 일치하지 않습니다.");
        }
        user.deleteUser(username);
    }

    @Transactional
    @Override
    public void updateUserToOwner(UserDetails userDetails, UserRoleUpdateOwnerRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));

        user.updateRoleOwner(request.getBusinessNumber());
    }

    @Override
    public UserInfoResponse getUserInfo(UserDetails userDetails) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));

        return UserInfoResponse.builder()
                .username(user.getUsername())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .isPublic(user.getIsPublic())
                .createdAt(user.getCreatedAt())
                .build();
    }

    @Transactional
    @Override
    public void updateUserToManager(UserDetailsImpl userDetails, UserRoleUpdateManagerRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));
        if (isCorrectSecretCode(request.getSecretCode(), MANAGER_SECRET_CODE)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        user.updateRole(MANAGER);
    }

    @Transactional
    @Override
    public void updateUserToMaster(UserDetailsImpl userDetails, UserRoleUpdateMasterRequest request) {
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("해당 username을 찾을 수 없습니다."));
        if (isCorrectSecretCode(request.getSecretCode(), MASTER_SECRET_CODE)) {
            throw new IllegalArgumentException("잘못된 요청입니다.");
        }
        user.updateRole(MASTER);
    }

    private boolean isCorrectSecretCode(String secretCode, String SECRET_CODE) {
        return !secretCode.equals(SECRET_CODE);
    }

    private String getUsername(UserDetails userDetails) {
        return userDetails.getUsername();
    }

    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    private boolean isUsernameDuplicated(String username) {
        return userRepository.existsByUsername(username);
    }

    private boolean isEmailDuplicated(String username) {
        return userRepository.existsByEmail(username);
    }
}
