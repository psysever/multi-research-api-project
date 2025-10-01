package com.research2.api.domain.auth.config;


import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// JWT를 이용한 인터셉터 구현
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(JwtInterceptor.class);

    @Autowired
    private JwtTokenProvider jwtTokenProvider; //JWT 유틸리티 객체 주입

    @Autowired
    public JwtInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
      

        // Get token
        // Method executed when a request comes in.
        String accessToken = jwtTokenProvider.getAccessToken(request); //Get access token from header


        //URI for logging
        String requestURI = request.getRequestURI();

        // When you are not a member (when you do not have an access token)
        if (accessToken != null) {
            if (jwtTokenProvider.validateToken(accessToken)) {
                logger.debug("This is valid token information URI : {}", requestURI);
            } else {
                //액세스 토큰이 유효하지 않을 시
                logger.debug("Invalid jwt token. uri : {}", requestURI);
                throw new CustomException(ErrorCodes.CommonErrorCode.EXPIRED_ACCESS_TOKEN);
            }
        }
        return true;
    }
}