package com.research2.api.domain.auth.config;


import com.research2.api.domain.global.exception.CustomException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;


//Extract the token in the header (Authorization) and if there is no problem, store it in SecurityContext and operate before the request.

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, java.io.IOException {

        String token = jwtTokenProvider.resolveToken(request);
        try {
            if (token != null && jwtTokenProvider.validateToken(token)) {
                Authentication auth = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
            filterChain.doFilter(request, response);
        } catch (CustomException e) {
            // When a CustomException occurs, write the response in JSON format.
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // HTTP 401 Unauthorized
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");

            // Write JSON response content
            String jsonResponse = String.format("{\"code\": %d, \"message\": \"%s\"}",
                    e.getCode(), e.getMessage());

            //Write JSON content to the response
            response.getWriter().write(jsonResponse);
        }

    }

}