package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.service.BookService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class RecommendKeywordController {
    private final BookService bookService;

    @GetMapping("/autocomplete_book")
    public ResponseEntity<String> autocomplete_book(@RequestParam("q") final String keyword) {
        List<String> bookNames = bookService.autocomplete_book(keyword);
        ObjectMapper mapper = new ObjectMapper();

        String resp = "";
        try {
            resp = mapper.writeValueAsString(bookNames);
        } catch (JsonProcessingException e) {
        }
        return new ResponseEntity<String>(resp, HttpStatus.OK);
    }

//    @GetMapping("/autocomplete_writer")
//    public ResponseEntity<String> autocomplete_writer(@RequestParam("q") final String keyword) {
//        List<String> bookNames = bookService.autocomplete_writer(keyword);
//        ObjectMapper mapper = new ObjectMapper();
//        String resp = "";
//        try {
//            resp = mapper.writeValueAsString(bookNames);
//        } catch (JsonProcessingException e) {
//        }
//        return new ResponseEntity<String>(resp, HttpStatus.OK);
//    }



}

