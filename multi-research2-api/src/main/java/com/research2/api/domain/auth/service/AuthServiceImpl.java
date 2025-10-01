package com.research2.api.domain.auth.service;


import com.research2.api.domain.auth.config.JwtTokenProvider;
import com.research2.api.domain.auth.dto.req.LoginReqDto;
import com.research2.api.domain.auth.dto.req.ReissueTokensReqDto;
import com.research2.api.domain.auth.dto.res.UserInfoResDto;
import com.research2.api.domain.auth.entity.Token;
import com.research2.api.domain.auth.repository.AuthRepositoryWithRedis;
import com.research2.api.domain.auth.repository.UserRepository;
import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.List;


@Service("AuthServiceImpl")
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService, UserDetailsService {

    private final UserRepository userRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthRepositoryWithRedis authRepositoryWithRedis;


    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        UserInfoResDto user = userRepository.findByUsername(identifier);
        if (user == null) {
            throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        // 유저의 권한을 설정하는 부분
        return new User(user.getUsername(), user.getPassword(), authorities);
    }


    @Transactional
    public Token authUser(LoginReqDto LoginReqDto) {
        UserInfoResDto user = userRepository.authUser(LoginReqDto);
        if (user == null) {
            throw new CustomException(ErrorCodes.UserErrorCode.USER_CAN_NOT_FIND);
        } else {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword());
            Authentication authentication = authenticationManagerBuilder.getObject()
                    .authenticate(authenticationToken);
            return createTokenWithRedis(authentication);
        }
    }


    //토큰의 유효성 검증
    public Boolean authorization(String token) {
        if (token != null && jwtTokenProvider.validateToken(token)) {
            return true;
        } else {
            throw new CustomException(ErrorCodes.CommonErrorCode.UNAUTHORIZED);
        }
    }

    //Verify token validity
    public Token reissueTokens(ReissueTokensReqDto ReissueTokensReqDto) {

        // 2.
        //Validate token with validateToken
        Token token = null;
        if (jwtTokenProvider.validateRefreshToken(ReissueTokensReqDto.getRefreshToken())) {
            String identifier = jwtTokenProvider.getId(ReissueTokensReqDto.getRefreshToken());
            UserDetails userDetails = loadUserByUsername(identifier);
            if (userDetails.getUsername().equals(identifier)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(
                        ReissueTokensReqDto.getRefreshToken());
                token = createTokenWithRedis(authentication);
            }
        } else {
            throw new CustomException(ErrorCodes.CommonErrorCode.INVALID_REFRESH_TOKEN);
        }
        return token;
    }

    private Token createTokenWithRedis(Authentication authentication) {
        String tokenUserId = (String) authentication.getPrincipal();
        Token tokens = jwtTokenProvider.generateToken(authentication);
        Claims claims = jwtTokenProvider.parseClaims(tokens.getRefreshToken()); //exp 값을 가져오는 부분
        if (claims != null && !claims.isEmpty()) {
            authRepositoryWithRedis.deleteById(tokenUserId);
        }
        Token token = Token.builder()
                .accessToken(tokens.getAccessToken())
                .refreshToken(tokens.getRefreshToken())
                .build();
        authRepositoryWithRedis.save(token);
        return tokens;
    }


}

