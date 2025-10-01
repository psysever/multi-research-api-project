package com.research2.api.domain.auth.config;


import com.research2.api.domain.auth.service.AuthService;
import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CustomAuthenticationProvider implements AuthenticationProvider {


    private final AuthService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String username = (String) authentication.getPrincipal();
        String password = authentication.getCredentials().toString();


        UserDetails user;

        user = authService.loadUserByUsername(username);
        if (!matchPassword(password, user.getPassword())) {
            throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
        }
        if (!user.isEnabled()) {
            throw new CustomException(ErrorCodes.UserErrorCode.USER_DOES_NOT_ENABLED);
        }
        return new UsernamePasswordAuthenticationToken(username, password, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }


    private boolean matchPassword(String loginPwd, String password) {
        return loginPwd.equals(password);
    }

}
