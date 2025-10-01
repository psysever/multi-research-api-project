package com.research2.api.domain.auth.config;


import com.research2.api.domain.auth.entity.Token;
import com.research2.api.domain.auth.repository.AuthRepositoryWithRedis;
import com.research2.api.domain.auth.repository.UserRepository;
import com.research2.api.domain.global.exception.CustomException;
import com.research2.api.domain.global.exception.error.ErrorCodes;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtTokenProvider.class);
    private final RedisTemplate<String, String> redisTemplate;
    private final UserRepository userRepository;

    @Value("${spring.jwt.secret}")
    private String secret;

    private Key secretKey;

    @PostConstruct
    public void init() {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }


    @Value("${spring.jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    public static final String AUTHORIZATION_HEADER = "Authorization"; //헤더 이름

    @Autowired
    private AuthRepositoryWithRedis authRepositoryWithRedis;


    public JwtTokenProvider(RedisTemplate<String, String> redisTemplate,
                            UserRepository userRepository) {
        this.redisTemplate = redisTemplate;
        this.userRepository = userRepository;
    }

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
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .setIssuedAt(now)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();

        return Token.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    //Token validation method
    public boolean validateToken(String accessToken) {
//Catch exceptions that occur after token parsing and return false if there is a problem, and true if there is a problem.
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken);
            return true;
        } catch (SignatureException e) {
            //When the signature is incorrect
            throw new CustomException(ErrorCodes.CommonErrorCode.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            // When the token expires
            throw new CustomException(ErrorCodes.CommonErrorCode.EXPIRED_ACCESS_TOKEN);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            // Handling when the token is not correctly configured
            System.out.println("Invalid token.");
            throw new CustomException(ErrorCodes.CommonErrorCode.UNAUTHORIZED);
        }
    }

    public boolean validateRefreshToken(String refreshToken) {
        //Catch exceptions that occur after token parsing and return false if there is a problem, and true if everything is OK.
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(refreshToken);
            Optional<Token> refreshTokenInRedis = authRepositoryWithRedis.findByRefreshToken(
                    refreshToken);
            // Returns whether the given token exists in Redis.
            return refreshTokenInRedis.isPresent();
        } catch (CustomException e) {
            throw new CustomException(ErrorCodes.CommonErrorCode.INVALID_REFRESH_TOKEN);
        } catch (IllegalArgumentException | MalformedJwtException e) {
            // Handling when the token is not correctly configured
            System.out.println("Invalid token.");
        }
        return false;
    }

    //A method to decrypt a JWT token and extract the information contained in the token.
    public Authentication getAuthentication(String token) {
        // 토큰 복호화
        Claims claims = parseClaims(token);

        if (claims.get("auth") == null) {
            throw new CustomException(ErrorCodes.CommonErrorCode.UNAUTHORIZED);
        }

        //
        //Get permission information from claims
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get("auth").toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    //Get the bearer token from the http header.
    public String resolveToken(HttpServletRequest req) {
        String bearerToken = req.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    //A method that extracts and returns member_id from a token.
    public String getId(String token) {
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
                .getSubject();
    }

    // A method that extracts and returns the name from the token.
    public String getName(String token) {
        System.out.println("getName");
        Claims name = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
                .getBody();

        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).getBody()
                .get("name").toString();
    }

    // This method extracts the access token through the Authorization Header in HttpServletRequest.
    public String getAccessToken(HttpServletRequest httpServletRequest) {
        String bearerToken = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }


    public Claims parseClaims(String accessToken) {
        try {
            Claims a = Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken)
                    .getBody();
            return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}