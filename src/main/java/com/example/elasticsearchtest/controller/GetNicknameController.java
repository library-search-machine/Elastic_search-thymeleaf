package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.dto.Response.ResponseDto;
import com.example.elasticsearchtest.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.JWT_NOT_PERMIT;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RequiredArgsConstructor
@RestController

public class GetNicknameController {
    private final TokenProvider tokenProvider;

    @RequestMapping(value="/getnickname", method=GET)
    @ResponseBody
    public String test(HttpServletRequest request) {
                Member member = TokenValidation(request);
        System.out.println(member.getNickName());
        System.out.println(ResponseDto.success(member.getNickName()));
        String str = member.getNickName();
        return str;
    }
    public Member TokenValidation( HttpServletRequest request){
        //토큰 검증과정
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
