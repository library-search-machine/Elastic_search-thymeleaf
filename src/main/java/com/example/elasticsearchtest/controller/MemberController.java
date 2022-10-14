package com.example.elasticsearchtest.controller;

import com.example.elasticsearchtest.dto.LoginRequestDto;
import com.example.elasticsearchtest.dto.MemberRequestDto;
import com.example.elasticsearchtest.response.ResponseDto;
import com.example.elasticsearchtest.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class MemberController {

    private final MemberService memberService;

    @RequestMapping(value = "/member/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto) {
        return memberService.createMember(requestDto);
    }

    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(requestDto, response);
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
