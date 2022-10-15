package com.example.elasticsearchtest.service;


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
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MongoDBService {

    private final MongodbRepository mongodbRepository;
    private final TokenProvider tokenProvider;

    @Transactional
    public ResponseDto<?> create_comment(BookReviewRequest bookReviewRequest, HttpServletRequest httpServletRequest) {
        if (null == httpServletRequest.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == httpServletRequest.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(httpServletRequest);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

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
    public ResponseDto<?> delete_comment(String id, HttpServletRequest httpServletRequest) {
        if (null == httpServletRequest.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == httpServletRequest.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(httpServletRequest);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        BooksReview booksReview = mongodbRepository.findById(id).get();
        if (booksReview.getNickname().equals(member.getNickName())) {
            return ResponseDto.fail("MEMBER_DIFFERENT","멤벅가 다릅니다.");
        }

        mongodbRepository.deleteById(id);//새로운 댓글을 저장하는 것임
        return ResponseDto.success("good");
    }

    @Transactional
    public ResponseDto<?> modify_comment(ModifyCommentDtoRequest request, HttpServletRequest httpServletRequest) {
        if (null == httpServletRequest.getHeader("RefreshToken")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }
        if (null == httpServletRequest.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(httpServletRequest);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        BooksReview booksReview = mongodbRepository.findById(request.getModify_id()).get();
        if (booksReview.getNickname().equals(member.getNickName())) {
            return ResponseDto.fail("MEMBER_DIFFERENT","멤벅가 다릅니다.");
        }

        booksReview.modify(request.getTitle(), request.getComment(), request.getStars());
        mongodbRepository.save(booksReview);
        return ResponseDto.success("good");
    }

    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("RefreshToken"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
