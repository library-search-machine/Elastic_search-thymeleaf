package com.example.elasticsearchtest.dto.Request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class writeBookRequestDto {
    private String title;
    private String content;
    private String authorId;
}
