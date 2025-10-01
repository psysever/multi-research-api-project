package com.research1.api.global.security.jwt;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/// / Interceptor implementation using JWT
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider; // Inject JWT utility object

    @Autowired
    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        // Get the token
        // This method runs when a request is received
        String accessToken = jwtTokenProvider.getAccessToken(request); // Get access token from header
        // URI for logging
        String requestURI = request.getRequestURI();

        // If the access token is valid
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                logger.debug("Valid token information. URI : {}", requestURI);
                return true;
            } else {
                // If the access token is invalid
                logger.debug("Invalid JWT token. URI : {}", requestURI);
                throw new IllegalArgumentException("Invalid token.");
//              throw new CustomException(CommonErrorCode.INVALID_TOKEN);
            }
        }
        return true;
    }
}
