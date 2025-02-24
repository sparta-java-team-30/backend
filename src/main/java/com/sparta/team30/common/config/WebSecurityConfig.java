package com.sparta.team30.common.config;

import com.sparta.team30.common.util.TokenProvider;
import com.sparta.team30.common.security.TokenAuthenticationFilter;
import com.sparta.team30.common.security.TokenGenerateFilter;
import com.sparta.team30.common.security.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {
    private final TokenProvider tokenProvider;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.authorizeHttpRequests((authorization)-> authorization
                .requestMatchers("/api/auth/signup", "/api/auth/signin",
                        "/h2-console/**", "/swagger-ui/**").permitAll()
                .requestMatchers("/api/auth").hasRole("MANAGER")
                .requestMatchers("/api/stores/**").permitAll()
                .requestMatchers("/api/categories/**").permitAll()
                .anyRequest().authenticated()
        );

        http.addFilterBefore(tokenAuthenticationFilter(), tokenGenerateFilter().getClass());
        http.addFilterBefore(tokenGenerateFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web) -> web.ignoring().requestMatchers(
                "/h2-console/**", "/swagger-ui/**", "/v3/api-docs/**"
                , "/swagger-resources/**"
        );
    }

    @Bean
    public TokenGenerateFilter tokenGenerateFilter() throws Exception {
        TokenGenerateFilter filter = new TokenGenerateFilter(tokenProvider);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        filter.setFilterProcessesUrl("/api/auth/signin");
        return filter;
    }

    @Bean
    public TokenAuthenticationFilter tokenAuthenticationFilter(){
        return new TokenAuthenticationFilter(tokenProvider, userDetailsService);
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
