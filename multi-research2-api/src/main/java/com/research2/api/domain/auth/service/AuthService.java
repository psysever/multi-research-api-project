package com.research2.api.domain.auth.service;


import com.research2.api.domain.auth.dto.req.LoginReqDto;
import com.research2.api.domain.auth.dto.req.ReissueTokensReqDto;
import com.research2.api.domain.auth.entity.Token;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;


public interface AuthService {

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    Token authUser(LoginReqDto loginReqDto);

    Boolean authorization(String token);


    Token reissueTokens(ReissueTokensReqDto reissueTokensReqDto);

}
