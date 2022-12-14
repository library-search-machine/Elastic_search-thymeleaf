package com.example.elasticsearchtest.domain;

import lombok.*;

import javax.persistence.Id;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BooksReview {
    @Id
    private String id;
    private String title;//코멘트 제목
    private String comment;//글 댓글
    private int stars;//별점
    private String isbn13;//그 저장된 isbn을 지정
    private String nickname;//작성자를 알아야함..ㅠㅠ
    private String bookName;


    //isbn으로 들어가서 해당 코멘트를 작성하고 하트도 보내고 이러쿵 저러쿵....
    public void modify(String title,String comment,int stars){
        this.title=title;
        this.comment=comment;
        this.stars=stars;
    }

}
