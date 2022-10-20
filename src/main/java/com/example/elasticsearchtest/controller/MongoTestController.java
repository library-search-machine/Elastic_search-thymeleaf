package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.dto.Response.ResponseDto;
import com.example.elasticsearchtest.jwt.TokenProvider;
import com.example.elasticsearchtest.repository.MongodbRepository;
import com.example.elasticsearchtest.request.BookReviewRequest;

import com.example.elasticsearchtest.request.DeleteCommentDtoRequest;
import com.example.elasticsearchtest.request.ModifyCommentDtoRequest;
import com.example.elasticsearchtest.service.MongoDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.JWT_NOT_PERMIT;

@RequiredArgsConstructor
@RestController
public class MongoTestController {
    private final MongodbRepository mongodbRepository;
    private final MongoDBService mongoDBService;
    private final TokenProvider tokenProvider;
    @PostMapping("/comment")
    public ResponseDto<?> create_comment(@RequestBody BookReviewRequest bookReviewRequest, HttpServletRequest request) {
        //여기서는 받고 이제 service 부분에서 가보자..
        //저장전에 Hrequest 확인.

        mongoDBService.create_comment(bookReviewRequest,request);
        return ResponseDto.success("good");
    }
    @DeleteMapping("/delete_comment")
    public ResponseDto<?> delete_comment(@RequestBody DeleteCommentDtoRequest deleteCommentDtoRequest, HttpServletRequest request) {
        //여기서는 받고 이제 service 부분에서 가보자..
        //저장전에 request 확인.
        mongoDBService.delete_comment(deleteCommentDtoRequest.getDelete_id(),request);
        return ResponseDto.success("good");

    }
    @PostMapping("/modify_comment")
    public ResponseDto<?> modify_comment(@RequestBody ModifyCommentDtoRequest modifyCommentDtoRequest, HttpServletRequest request) {
        //여기서는 받고 이제 service 부분에서 가보자..
        //저장전에 request 확인.
        mongoDBService.modify_comment(modifyCommentDtoRequest,request);
        return ResponseDto.success("good");
    }
    @GetMapping("/get")
    public ResponseDto<?> get_comment() {
        //여기서는 받고 이제 service 부분에서 가보자..
        List<BooksReview> all = mongodbRepository.findAll();
        for (BooksReview booksReview : all) {
            System.out.println("booksReview.getIsbn13() = " + booksReview.getIsbn13());
            System.out.println("booksReview.getNickname() = " + booksReview.getNickname());
            
        }
        return ResponseDto.success("good");

    }
    @DeleteMapping("/deleteAll")
    public ResponseEntity<String> delete(HttpServletRequest request) {
        mongodbRepository.deleteAll();
        return ResponseEntity.ok("delete good");
    }


    public Member TokenValidation(HttpServletRequest request){
        //토큰 검증과정
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
