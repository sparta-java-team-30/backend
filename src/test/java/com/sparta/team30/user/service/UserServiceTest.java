package com.sparta.team30.user.service;

import com.sparta.team30.common.exception.UserEmailAlreadyExistsException;
import com.sparta.team30.common.exception.UsernameAlreadyExistsException;
import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.dto.UserInfoUpdateRequestDto;
import com.sparta.team30.user.dto.UserSignUpRequestDto;
import com.sparta.team30.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserDetailsImpl userDetails;

    @InjectMocks
    private UserServiceImpl userService;

//    private static final String MASTER_SECRET_CODE = "master123";
//    private static final String MANAGER_SECRET_CODE = "manager123";

    @BeforeEach
    void setUp(){
//        ReflectionTestUtils.setField(userDetails, "MASTER_SECRET_CODE", MASTER_SECRET_CODE);
//        ReflectionTestUtils.setField(userDetails, "MANAGER_SECRET_CODE", MANAGER_SECRET_CODE);


    }

    @DisplayName("이메일이 중복되어 회원 가입을 실패한다.")
    @Test
    void test1() {
        // Given
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .username("user1")
                .email("user1@example.com")
                .nickname("nickname")
                .password("Password1!")
                .build();

        when(userRepository.existsByEmail("user1@example.com")).thenReturn(true);
        // when & then
        assertThrows(UserEmailAlreadyExistsException.class,
                () -> userService.signup(requestDto));
    }

    @DisplayName("username 이 중복되어 회원 가입을 실패한다.")
    @Test
    void test2() {
        // Given
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .username("user1")
                .email("user1@example.com")
                .nickname("nickname")
                .password("Password1!")
                .build();

        when(userRepository.existsByUsername("user1")).thenReturn(true);

        // when & then
        assertThrows(UsernameAlreadyExistsException.class,
                () -> userService.signup(requestDto));
    }

    @DisplayName("회원 가입에 성공한다.")
    @Test
    void test3() {
        // given
        UserSignUpRequestDto requestDto = UserSignUpRequestDto.builder()
                .username("user1")
                .email("user1@example.com")
                .nickname("nickname")
                .password("Password1!")
                .build();

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");
        // When
        userService.signup(requestDto);

        // Then
        verify(userRepository, times(1)).save(any(User.class));
//        verify(userRepository, times(1)).updateCreatedBy(eq("SYSTEM"), any(Long.class));

    }

    @DisplayName("회원 정보 수정에 성공한다.")
//    @WithUserDetails(value = "user1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
    @Test
    void test4() {
        // given
        User user = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password("password")
                .nickname("nickname")
                .isPublic(true)
                .build();

        UserInfoUpdateRequestDto requestDto = UserInfoUpdateRequestDto.builder()
                .email("user1@example.com")
                .nickname("nickname")
                .isPublic(false)
                .build();
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));

        userDetails = new UserDetailsImpl(user);

        // When
        userService.userInfoUpdate(userDetails, requestDto);

        // Then
        assertEquals("nickname", user.getNickname());
        assertFalse(user.getIsPublic());
    }
}
