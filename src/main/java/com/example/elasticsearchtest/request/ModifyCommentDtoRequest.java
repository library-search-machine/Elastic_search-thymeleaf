package com.example.elasticsearchtest.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ModifyCommentDtoRequest {
    private  String title;
    private String comment;
    private int stars;
    private String modify_id;
}
