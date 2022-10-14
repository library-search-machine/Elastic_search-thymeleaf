package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.dto.Response.ResponseDto;
import com.example.elasticsearchtest.repository.MongodbRepository;
import com.example.elasticsearchtest.request.BookReviewRequest;
import com.example.elasticsearchtest.request.ModifyCommentDtoRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class MongoDBService {

    private final MongodbRepository mongodbRepository;

    @Transactional
    public ResponseDto<?> create_comment(BookReviewRequest bookReviewRequest) {
        BooksReview booksReview = BooksReview.builder() //새로운 코멘트를 생성하고 이제 이것을 넣어줌
                .title(bookReviewRequest.getTitle())
                .comment(bookReviewRequest.getComment())
                .nickname("nickname")
                .bookName(bookReviewRequest.getBookName())
                .isbn13(bookReviewRequest.getIsbn13())
                .stars(bookReviewRequest.getStars())
                .build();
        mongodbRepository.save(booksReview);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }

    @Transactional
    public ResponseDto<?> delete_comment(String id) {
        mongodbRepository.deleteById(id);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }

    @Transactional
    public ResponseDto<?> modify_comment(ModifyCommentDtoRequest request) {
        BooksReview booksReview = mongodbRepository.findById(request.getModify_id()).get();
        booksReview.modify(request.getTitle(), request.getComment(), request.getStars());
        mongodbRepository.save(booksReview);
        return ResponseDto.success("good");
    }


}
