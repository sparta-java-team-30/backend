package com.sparta.team30.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.team30.common.config.WebSecurityConfig;
import com.sparta.team30.common.security.UserDetailsImpl;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.domain.UserRoleEnum;
import com.sparta.team30.user.dto.*;
import com.sparta.team30.user.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@WebMvcTest(value = UserController.class,
excludeFilters = {
        @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = WebSecurityConfig.class
        )
})
public class UserControllerTest {
    private MockMvc mvc;

    private Principal principal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    UserServiceImpl userService;

    @Autowired
    private DefaultAuthenticationEventPublisher authenticationEventPublisher;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유저 생성
        String username = "user1";
        String password = "Password1!";
        String email = "email1@email.com";
        String nickname = "nickname";
        UserRoleEnum role = UserRoleEnum.USER;

        User testUser = User.builder()
                .username(username)
                .password(password)
                .nickname(nickname)
                .email(email)
                .role(role)
                .isPublic(true)
                .build();

        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        principal = new UsernamePasswordAuthenticationToken(testUserDetails, "", testUserDetails.getAuthorities());
    }

    @DisplayName("회원 가입에 성공한다.")
    @Test
    void test1() throws Exception {
        UserSignUpRequestDto requestDto = UserSignUpRequestDto
                .builder()
                .email("email1@email.com")
                .password("Password1!")
                .username("user1")
                .nickname("nickname")
                .build();

        mvc.perform(post("/api/auth/signup")
                .content(objectMapper.writeValueAsString(requestDto))
                .contentType("application/json"))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @DisplayName("로그인에 성공한다.")
    @Test
    void test2() throws Exception {
        UserSignInRequestDto requestDto = new UserSignInRequestDto("user1", "Password1!");

        mvc.perform(post("/api/auth/signin")
                .contentType("application/json")
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andDo(print());
    }

// 회원 로그인은 Filter에서 작동하는 중
// 따라서 Filter를 우회하기 때문에 건너뜀
//    @DisplayName("로그인에 성공한다.")
//    @Test
//    void test2() throws Exception {
//        UserSignInRequestDto requestDto = UserSignInRequestDto.builder()
//                .username("user1")
//                .password("Password1!")
//                .build();
//
//        mvc.perform(post("/api/auth/signin")
//                .contentType("application/json")
//                        .content(objectMapper.writeValueAsString(requestDto)))
//                .andExpect(status().isOk())
//                .andDo(print());
//    }

    @DisplayName("회원 정보 조회에 성공한다.")
    @Test
    void test3() throws Exception {
        // given
        this.mockUserSetup();

        when(userService.getUserInfo(any(UserDetails.class)))
                .thenReturn(new UserInfoResponse(
                        "user1", "email1@email.com", "nickname", true, null));
        // when & then
        mvc.perform(get("/api/auth/info")
                .contentType("application/json")
                .principal(principal))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("user1"))
                .andDo(print());
    }

    @DisplayName("회원 정보 수정에 성공한다.")
    @Test
    void testUpdateUserInfo() throws Exception {
        // 인증된 사용자 설정
        mockUserSetup();
        // UserInfoUpdateRequestDto의 필드(ex: nickname, email, isPublic)는 실제 구현에 맞게 수정
        UserInfoUpdateRequestDto updateDto = UserInfoUpdateRequestDto.builder()
                .nickname("newNickname")
                .email("newEmail@example.com")
                .isPublic(false)
                .build();

        mvc.perform(patch("/api/auth")
                        .content(objectMapper.writeValueAsString(updateDto))
                        .contentType("application/json")
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }

    @DisplayName("회원 탈퇴에 성공한다.")
    @Test
    void testDeleteUser() throws Exception {
        // 인증된 사용자 설정
        mockUserSetup();
        // UserDeleteRequestDto의 필드(ex: password)는 실제 구현에 맞게 수정
        UserDeleteRequestDto deleteDto = new UserDeleteRequestDto("Password1!");

        mvc.perform(delete("/api/auth")
                        .content(objectMapper.writeValueAsString(deleteDto))
                        .contentType("application/json")
                        .principal(principal))
                .andExpect(status().isOk())
                .andDo(print());
    }
}
