package com.example.elasticsearchtest.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookReviewResponse {
    private String id;
    private String title;//코멘트 제목
    private String comment;//글 댓글
    private int stars;//별점
    private String isbn13;//그 저장된 isbn을 지정
    private String nickname;//작성자를 알아야함..ㅠㅠ
    private String bookName;
    //코멘트 제목 내용 벌점 책이름 isbn id..?
}
