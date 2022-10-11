package com.example.elasticsearchtest.controller;

import com.example.elasticsearchtest.dto.Request.LoginRequestDto;
import com.example.elasticsearchtest.dto.Response.MemberResponseDto;
import com.example.elasticsearchtest.service.RegisterService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;

//회원가입,로그인 컨트롤러
@RequiredArgsConstructor
@RestController
public class RegisterController {
    private final RegisterService registerService;
    @GetMapping("/register")
    public ResponseEntity<String> test(@RequestParam("id") String id,@RequestParam("password") String password ) {

        registerService.registerId(id,password);
        return ResponseEntity.ok( "회원가입  성공");
    }

    //로그인
    @PostMapping(value = "/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody @Valid LoginRequestDto requestDto, HttpServletResponse response) {
        return registerService.login(requestDto, response);
    }

    //로그아웃
    @GetMapping(value = "/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return registerService.logout(request);
    }
}
