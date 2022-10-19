package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.Errorhandler.BusinessException;
import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.domain.Member;
import com.example.elasticsearchtest.dto.Response.ResponseDto;
import com.example.elasticsearchtest.jwt.TokenProvider;
import com.example.elasticsearchtest.repository.MongodbRepository;
import com.example.elasticsearchtest.request.BookReviewRequest;
import com.example.elasticsearchtest.request.ModifyCommentDtoRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;

import static com.example.elasticsearchtest.Errorhandler.ErrorCode.JWT_NOT_PERMIT;


@Service
@RequiredArgsConstructor
public class MongoDBService {

    private final MongodbRepository mongodbRepository;
    private final TokenProvider tokenProvider;
    @Transactional
    public ResponseDto<?> create_comment(BookReviewRequest bookReviewRequest, HttpServletRequest request) {
        //토큰 검증과정
        Member member= TokenValidation(request);

        BooksReview booksReview = BooksReview.builder() //새로운 코멘트를 생성하고 이제 이것을 넣어줌
                .title(bookReviewRequest.getTitle())
                .comment(bookReviewRequest.getComment())
                .nickname(member.getNickName())
                .bookName(bookReviewRequest.getBookName())
                .isbn13(bookReviewRequest.getIsbn13())
                .stars(bookReviewRequest.getStars())
                .build();
        mongodbRepository.save(booksReview);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }

    @Transactional
    public ResponseDto<?> delete_comment(String id, HttpServletRequest request) {
        //토큰 검증과정
        TokenValidation(request);

        mongodbRepository.deleteById(id);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }

    @Transactional
    public ResponseDto<?> modify_comment(ModifyCommentDtoRequest request, HttpServletRequest httpServletRequest) {
        //토큰 검증과정
        TokenValidation(httpServletRequest);

        BooksReview booksReview = mongodbRepository.findById(request.getModify_id()).get();
        booksReview.modify(request.getTitle(), request.getComment(), request.getStars());
        mongodbRepository.save(booksReview);
        return ResponseDto.success("good");
    }

    public Member TokenValidation(HttpServletRequest request){
        //토큰 검증과정
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            throw new BusinessException("잘못된 JWT 토큰입니다", JWT_NOT_PERMIT);
        }
        return tokenProvider.getMemberFromAuthentication();
    }
}
