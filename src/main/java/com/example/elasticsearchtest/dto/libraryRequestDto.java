package com.example.elasticsearchtest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class libraryRequestDto {

    private String bookName;
    private String authors;
    private String publisher;

}
