package com.sparta.team30.user.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

public class MockSpringSecurityFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        SecurityContextHolder.getContext()
                .setAuthentication((Authentication) ((HttpServletRequest) request).getUserPrincipal());
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        SecurityContextHolder.clearContext();
    }
}
