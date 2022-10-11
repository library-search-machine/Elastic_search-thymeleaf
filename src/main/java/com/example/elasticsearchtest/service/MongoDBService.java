package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.request.BookReviewRequest;
import com.example.elasticsearchtest.response.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MongoDBService {

    private final MongoRepository mongoRepository;

    @Transactional
    public ResponseDto<?> create_comment(BookReviewRequest bookReviewRequest) {
        BooksReview booksReview = BooksReview.builder() //새로운 코멘트를 생성하고 이제 이것을 넣어줌
                .title(bookReviewRequest.getTitle())
                .comment(bookReviewRequest.getComment())
                .isbn13(bookReviewRequest.getIsbn13())
                .stars(bookReviewRequest.getStars())
                .build();
        mongoRepository.save(booksReview);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }








}
