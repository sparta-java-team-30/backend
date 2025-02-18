package com.sparta.team30.user.service;

import com.sparta.team30.common.exception.UserEmailAlreadyExistsException;
import com.sparta.team30.common.exception.UsernameAlreadyExistsException;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.dto.UserSignUpRequestDto;
import com.sparta.team30.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private static final String DEFAULT_USER_CREATED_BY = "SYSTEM";
    private static final boolean DEFAULT_IS_PUBLIC = true;

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
