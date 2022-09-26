package com.example.elasticsearchtest.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto2 {
    private String bookName;
    private String authors;
    private String publisher;
    private String publicationYear;
    private String bookImageURL;
    private String description;
    private String class_nm;
    private Set<String> LibraryList;
}
