package com.example.elasticsearchtest.response;



import com.example.elasticsearchtest.domain.LibraryEs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.awt.print.Book;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private Long id;
    private String title;

    private String writer;

    private String publisher;
    private String vol;

    private String isbn;
    private String library;

    public  Page<BookResponseDto> toDtoList(Page<LibraryEs> postList){
        String match = "[^\uAC00-\uD7A30-9a-zA-Z\\s]";//특수 문자제거
        Page<BookResponseDto> ResponsePostList = postList.map(m -> BookResponseDto.builder()
                .title(m.getBookName().replaceAll(match,""))
                .writer(m.getAuthors())
                .publisher(m.getPublisher())
                .isbn(m.getIsbn13())
                .vol(m.getVol())
                .library(m.getLibraryName())
                .build()
        );
        return ResponsePostList;
    }
}