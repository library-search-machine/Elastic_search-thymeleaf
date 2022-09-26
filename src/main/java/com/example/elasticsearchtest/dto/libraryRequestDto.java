package com.example.elasticsearchtest.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class libraryRequestDto {
    private String bookName;

    private String authors;
    private String classNum;
    private String isbn13;
    private String libraryName;
    private String publicationYear;
    private String publisher;
    private String vol;

}
