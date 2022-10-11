package com.example.elasticsearchtest.request;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewRequest {
    private String title;//코멘트 제목
    private String comment;//글 댓글
    private int stars;//별점
    private String isbn13;//그 저장된 isbn을 지정

}
