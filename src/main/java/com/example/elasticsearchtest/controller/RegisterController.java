package com.example.elasticsearchtest.controller;

import com.example.elasticsearchtest.dto.Request.LoginRequestDto;
import com.example.elasticsearchtest.dto.Response.MemberResponseDto;
import com.example.elasticsearchtest.service.RegisterService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

//회원가입,로그인 컨트롤러
@RequiredArgsConstructor
@Controller
public class RegisterController {
    private final RegisterService registerService;
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestParam("id") String id,@RequestParam("password") String password ) {
        System.out.println(id+" "+password);
        registerService.registerId(id,password);
        return ResponseEntity.ok( "회원가입  성공");
    }
    @PostMapping("/register/exists")
    public ResponseEntity<String> register_exists(@RequestParam("id") String id) {
        System.out.println(id);
       return  registerService.register_exists(id);
    }
    //로그인
    @PostMapping(value = "/login")
    public ResponseEntity<MemberResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
        System.out.println(loginRequestDto.getId()+" "+loginRequestDto.getPassword());
        return registerService.login(loginRequestDto, response);
    }
    @GetMapping("/page/login")
    public String login(Model model, HttpServletRequest request,HttpServletResponse response) {
        return "login";
    }
    //로그아웃
    @GetMapping(value = "/page/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        return registerService.logout(request);
    }
}
