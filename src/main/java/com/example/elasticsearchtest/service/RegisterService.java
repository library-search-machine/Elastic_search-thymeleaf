package com.example.elasticsearchtest.service;

import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.dto.Request.LoginRequestDto;
import com.example.elasticsearchtest.dto.Response.MemberResponseDto;
import com.example.elasticsearchtest.dto.TokenDto;
import com.example.elasticsearchtest.jwt.TokenProvider;
import com.example.elasticsearchtest.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.elasticsearchtest.domain.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import java.util.Optional;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.*;

//회원가입, 로그인을 위한 서비스
@Service
@RequiredArgsConstructor
public class RegisterService {
    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;
    @Transactional
    public void registerId(String id, String password) {
        if(isPresentMember(id, true)!=null){
            throw  new BusinessException("회원가입 실패",EMAIL_INPUT_INVALID);
        }
        Member member = new Member(id,password, passwordEncoder);
        System.out.println(member.getId() +" "+member.getNickName());
        memberRepository.save(member);
    }

    @Transactional
    public ResponseEntity<String> register_exists(String id) {
        if(isPresentMember(id, true)!=null){
            throw  new BusinessException("이미 중복된 아이디 입니다.",EMAIL_INPUT_INVALID);
        }
        else
            return ResponseEntity.ok( "good");
    }
    public ResponseEntity<MemberResponseDto> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getId(), false);
        member.validatePassword(passwordEncoder, requestDto.getPassword());
        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);
        System.out.println(SecurityContextHolder.getContext().getAuthentication()+"avc");
        return ResponseEntity.ok(new MemberResponseDto(tokenDto));
    }
    public ResponseEntity<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        Member member = tokenProvider.getMemberFromAuthentication();

        return tokenProvider.deleteRefreshToken(member);
    }

    //이미 가입되어있는지 유무확인. 또는 존재하는 멤버인지 확인
    @Transactional
    public Member isPresentMember(String id, boolean bool) {
        Optional<Member> optionalMember = memberRepository.findByNickName(id);
        if (!bool) {
            return optionalMember.orElseThrow(
                    () -> new BusinessException("로그인 실패.", LOGIN_INPUT_INVALID)
            );
        } else {
            return optionalMember.orElse(null);
        }

    }
    //프론트에 헤더 날리기
    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("RefreshToken", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }
}
