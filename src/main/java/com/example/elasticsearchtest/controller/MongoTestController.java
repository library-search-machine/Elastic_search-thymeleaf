package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.dto.Response.ResponseDto;
import com.example.elasticsearchtest.repository.MongodbRepository;
import com.example.elasticsearchtest.request.BookReviewRequest;

import com.example.elasticsearchtest.request.DeleteCommentDtoRequest;
import com.example.elasticsearchtest.request.ModifyCommentDtoRequest;
import com.example.elasticsearchtest.service.MongoDBService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class MongoTestController {
    private final MongodbRepository mongodbRepository;
    private final MongoDBService mongoDBService;
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
    public ResponseEntity<String> delete(HttpServletRequest Hrequest) {
        mongodbRepository.deleteAll();
        return ResponseEntity.ok("delete good");
    }

}
