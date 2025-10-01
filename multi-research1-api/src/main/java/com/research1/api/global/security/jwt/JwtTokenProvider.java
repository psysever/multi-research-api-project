package com.research1.api.global.security.jwt;


import com.research1.api.global.exception.error.ErrorCodes.CommonErrorCode;
import com.research1.api.global.exception.CustomException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component

public class JwtTokenProvider {
    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);

    // Java JWT (JSON Web Token) 라이브러리인 JJWT에서 제공하는 API를 사용하여, HS256(HMAC SHA-256) 알고리즘을 사용하는 시크릿 키를 생성
    private static final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    //accessToken 만료시간 설정
    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${spring.jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;
    public static final String AUTHORIZATION_HEADER = "Authorization"; //헤더 이름


    // 유저 정보를 가지고 AccessToken, RefreshToken 을 생성하는 메서드
    public Token generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        Date now = new Date();
        Date accessTokenExpiresIn = new Date(now.getTime() + accessExpirationTime);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .setExpiration(accessTokenExpiresIn)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
        // Refresh Token 생성
        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + refreshExpirationTime))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    /**
     * Refresh 토큰 생성
     */
    public String createRefreshToken(Authentication authentication) {
        Claims claims = Jwts.claims().setSubject(authentication.getName());
        Date now = new Date();
        Date expireDate = new Date(now.getTime() + refreshExpirationTime);

        String refreshToken = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expireDate)
                .signWith(secretKey)
                .compact();


        return refreshToken;
    }


    //토큰 유효성 검증 메서드
    public boolean validateToken(String token) {
        //토큰 파싱 후 발생하는 예외를 캐치하여 문제가 있으면 false, 정상이면 true 반환
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (SignatureException e) {
            throw new CustomException(CommonErrorCode.INVALID_TOKEN);
        } catch (ExpiredJwtException e) {
            // 토큰이 만료됐을 때
            throw new CustomException(CommonErrorCode.INVALID_TOKEN);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            // 토큰이 올바르게 구성되지 않았을 때 처리
            throw new CustomException(CommonErrorCode.INVALID_TOKEN);
        }
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다.");
        }


        // 클레임에서 권한 정보 가져오기
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication 리턴
        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    /**
     * http 헤더로부터 bearer 토큰을 가져옴.
     */
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰에서 member_id를 추출하여 반환하는 메소드
    public String getId(String token) {
        System.out.println("getId");

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().getSubject();
    }

    // 토큰에서 name을 추출하여 반환하는 메소드
    public String getName(String token) {
        System.out.println("getName");

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody().get("name").toString();
    }

    // HttpServletRequest에서 Authorization Header를 통해 access token을 추출하는 메서드입니다.
    public String getAccessToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // Header Access Token 설정
    public void setHeaderAccessToken(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION_HEADER, accessToken);
    }

    // 회원 or 비회원일 경우 URI로 Redirect 시키는 함수
    public String determineRedirectURI(HttpServletRequest httpServletRequest, String memberURI, String nonMemberURI) {
        String token = getAccessToken(httpServletRequest);
        if (token == null) {
            return nonMemberURI; // Member URI Redirect
        } else {
            return memberURI; // Nom Member URI Redirect
        }
    }

    public Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }
}