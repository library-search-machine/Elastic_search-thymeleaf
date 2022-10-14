package com.example.elasticsearchtest.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCommentDtoRequest {
    private String delete_id;
}
