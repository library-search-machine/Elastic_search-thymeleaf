package com.example.elasticsearchtest.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.RequestParam;

@Getter
@Setter
@Builder
public class libraryRequestDto {

    private String bookName;
    private String authors;
    private String publisher;
    private String firstPublication;
    private String endPublication;
    private String genre;
    private String library;

}
