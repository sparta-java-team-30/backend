package com.sparta.team30.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.team30.common.util.TokenProvider;
import com.sparta.team30.user.domain.User;
import com.sparta.team30.user.dto.UserSignInRequestDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Duration;
import java.util.Collections;

@RequiredArgsConstructor
public class TokenGenerateFilter extends UsernamePasswordAuthenticationFilter {
    private final TokenProvider tokenProvider;
    private final static Duration EXPIRED_AT = Duration.ofHours(2);
    private final static String TOKEN_PREFIX = "Bearer ";
    private final static String HEADER_AUTHORIZATION = "Authorization";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserSignInRequestDto userSignInRequestDto = new ObjectMapper().readValue(request.getInputStream(),
                    UserSignInRequestDto.class);
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userSignInRequestDto.getUsername(),
                            userSignInRequestDto.getPassword(),
                            Collections.emptyList()
                    )
            );

        } catch (Exception e) {
            throw new AuthenticationException(e.getMessage()){};
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) {

        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        String token = tokenProvider.generateToken(user, EXPIRED_AT);
        response.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + token);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) {
        response.setStatus(401);
    }
}
