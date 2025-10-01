package com.research1.api.global.security.jwt;

import com.research1.api.global.exception.CustomException;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

/// **
// * 헤더(Authorization)에 있는 토큰을 꺼내 이상이 없는 경우 SecurityContext에 저장
// * Request 이전에 작동
// */

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException, CustomException {
        // 1. Request Header 에서 JWT 토큰 추출
        String token = jwtTokenProvider.resolveToken(request);
        try {
            // 2. validateToken 으로 토큰 유효성 검사
            if (token != null && jwtTokenProvider.validateToken(token)) {
                // 4. 토큰이 유효할 경우 토큰에서 Authentication 객체를 가지고 와서 SecurityContext 에 저장
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // CustomException 발생 시 응답을 JSON 형식으로 작성
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // JSON 응답 내용 작성
            String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\"}",
                    e.getCode(), e.getMessage());

            // 응답에 JSON 내용 쓰기
            response.getWriter().write(jsonResponse);
        }
    }

}