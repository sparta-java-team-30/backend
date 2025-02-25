package com.sparta.team30.user.service;

import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.dto.UserDeleteRequestDto;
import com.sparta.team30.user.dto.UserInfoResponse;
import com.sparta.team30.user.dto.UserInfoUpdateRequestDto;
import com.sparta.team30.user.dto.UserSignUpRequestDto;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(TestConfiguration.class)
public class UserServiceIntegrationTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("회원가입 시 중복 아이디, 이메일 체크 및 사용자 생성")
    @Order(1)
    void signupTest() {
        // given
        UserSignUpRequestDto signUpRequest = UserSignUpRequestDto.builder()
                .username("testuser")
                .email("testuser@example.com")
                .password("password")
                .nickname("Tester")
                .build();

        // when
        userService.signup(signUpRequest);

        // then: 사용자 생성 확인 및 필드 값 검증
        User savedUser = userRepository.findByUsername("testuser")
                .orElseThrow(() -> new IllegalStateException("회원가입 실패"));
        assertThat(savedUser.getUsername()).isEqualTo("testuser");
        assertThat(savedUser.getEmail()).isEqualTo("testuser@example.com");
        assertThat(passwordEncoder.matches("password", savedUser.getPassword())).isTrue();
        assertThat(savedUser.getRole()).isEqualTo(UserRoleEnum.USER);
    }

    @Test
    @DisplayName("사용자 정보 업데이트 테스트")
    @Order(2)
    void userInfoUpdateTest() {
        UserSignUpRequestDto signUpRequest = UserSignUpRequestDto.builder()
                .username("updateuser")
                .email("updateuser@example.com")
                .password("password")
                .nickname("Updater")
                .build();
        userService.signup(signUpRequest);

        var updateRequest = UserInfoUpdateRequestDto.builder()
                .email("newemail@example.com")
                .nickname("NewNickname")
                .isPublic(false)
                .build();

        var userDetails = new com.sparta.team30.common.security.UserDetailsImpl(
                userRepository.findByUsername("updateuser").get()
        );

        // when
        userService.userInfoUpdate(userDetails, updateRequest);

        // then
        User updatedUser = userRepository.findByUsername("updateuser")
                .orElseThrow(() -> new IllegalStateException("업데이트 실패"));
        assertThat(updatedUser.getEmail()).isEqualTo("newemail@example.com");
        assertThat(updatedUser.getNickname()).isEqualTo("NewNickname");
        assertThat(updatedUser.getIsPublic()).isFalse();
    }

    @Test
    @DisplayName("사용자 삭제 테스트 - 비밀번호 검증")
    @Order(3)
    void deleteUserTest() {
        // given: 회원가입
        UserSignUpRequestDto signUpRequest = UserSignUpRequestDto.builder()
                .username("deleteuser")
                .email("deleteuser@example.com")
                .password("password")
                .nickname("Deleter")
                .build();

        userService.signup(signUpRequest);
        User user = userRepository.findByUsername("deleteuser").get();
        var userDetails = new com.sparta.team30.common.security.UserDetailsImpl(user);

        var deleteRequest = UserDeleteRequestDto.builder()
                .password("password")
                .build();


        // when
        userService.deleteUser(userDetails, deleteRequest);

        // then: 실제로 삭제 처리
        User deletedUser = userRepository.findByUsername("deleteuser")
                .orElseThrow(() -> new IllegalStateException("사용자 조회 실패"));
        assertThat(deletedUser.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("회원 정보 조회 테스트")
    @Order(4)
    void getUserInfoTest() {
        // given: 회원가입
        UserSignUpRequestDto signUpRequest = UserSignUpRequestDto.builder()
                .username("infoUser")
                .email("info@example.com")
                .password("password")
                .nickname("Info")
                .build();
        userService.signup(signUpRequest);
        var userDetails = new com.sparta.team30.common.security.UserDetailsImpl(
                userRepository.findByUsername("infoUser").get()
        );

        // when
        UserInfoResponse response = userService.getUserInfo(userDetails);

        // then
        assertThat(response.getUsername()).isEqualTo("infoUser");
        assertThat(response.getEmail()).isEqualTo("info@example.com");
        assertThat(response.getNickname()).isEqualTo("Info");
    }
}
