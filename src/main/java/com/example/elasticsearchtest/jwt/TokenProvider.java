package com.example.elasticsearchtest.jwt;

import com.example.elasticsearchtest.Auth.Authority;
import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.Errorhandler.ErrorCode;
import com.example.elasticsearchtest.domain.RefreshToken;
import com.example.elasticsearchtest.domain.UserDetailsImpl;
import com.example.elasticsearchtest.dto.TokenDto;
import com.example.elasticsearchtest.repository.RefreshTokenRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Date;
import java.util.Optional;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.JWT_NOT_PERMIT;
import static com.example.elasticsearchtest.Errorhandler.ErrorCode.LOGIN_INPUT_INVALID;

import com.example.elasticsearchtest.domain.Member;


@Slf4j
@Component
public class TokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 30;         //30분
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 1000 * 60 * 60 * 24 * 7;     //7일

    private final Key key;

    private final RefreshTokenRepository refreshTokenRepository;

    public TokenProvider(@Value("${jwt.secret}") String secretKey,
                         RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public TokenDto generateTokenDto(Member member) {
        long now = (new Date().getTime());

        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(member.getNickName())
                .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString())
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(member.getNickName())
                .claim(AUTHORITIES_KEY, Authority.ROLE_MEMBER.toString())
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        RefreshToken refreshTokenObject = new RefreshToken(member,refreshToken);

        saveRepo(refreshTokenObject);



        return TokenDto.builder()
                .grantType(BEARER_PREFIX)
                .accessToken(accessToken)
                .accessTokenExpiresIn(accessTokenExpiresIn.getTime())
                .refreshToken(refreshToken)
                .build();

    }
    @Cacheable(value="member",key="#refreshTokenObject.id",cacheManager = "cacheManager1")
    public void saveRepo(RefreshToken refreshTokenObject){
        refreshTokenRepository.save(refreshTokenObject);

    }
    @Cacheable(value="member",key="#refreshTokenObject.id",cacheManager = "cacheManager1")
    public RefreshToken getRepo(RefreshToken refreshTokenObject){
        Optional<RefreshToken> refreshToken =refreshTokenRepository.findByToken(refreshTokenObject);
        return refreshToken.orElseThrow(
                () -> new BusinessException("토큰이 유효하지않음.", LOGIN_INPUT_INVALID));


    }


    public Member getMemberFromAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.
                isAssignableFrom(authentication.getClass())) {
            throw new BusinessException("Member가 존재하지 않습니다.", ErrorCode.MEMBER_NOT_EXIST);
        }
        return ((UserDetailsImpl) authentication.getPrincipal()).getMember();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");

        } catch (ExpiredJwtException e) {
            //로컬스토리지에서 지우라는 명령이 필요함
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    @Transactional(readOnly = true)
    public RefreshToken isPresentRefreshToken(Member member) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findById(member.getId());
        return optionalRefreshToken.orElse(null);
    }

    @CacheEvict(value="member",key="#member.id",cacheManager = "cacheManager1")
    public ResponseEntity<?> deleteRefreshToken(Member member) {
        RefreshToken refreshToken = isPresentRefreshToken(member);
        if (null == refreshToken) {
            throw new BusinessException("TOKEN_NOT_FOUND", JWT_NOT_PERMIT);

        }
        refreshTokenRepository.delete(refreshToken);
        return new ResponseEntity<>("success", HttpStatus.OK);

    }
}