package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.request.BookReviewRequest;
import com.example.elasticsearchtest.response.ResponseDto;
import com.example.elasticsearchtest.service.MongoDBService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Book;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RestController
public class MongoTestController {
    private final MongoRepository mongoRepository;
    private final MongoDBService mongoDBService;

    @PostMapping("/comment")
    public ResponseDto<?> create_comment(@RequestBody BookReviewRequest request) {
        //여기서는 받고 이제 service 부분에서 가보자..
        mongoDBService.create_comment(request);
        return ResponseDto.success("good");

    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> delete() {
        mongoRepository.deleteAll();
        return ResponseEntity.ok("delete good");
    }

}
