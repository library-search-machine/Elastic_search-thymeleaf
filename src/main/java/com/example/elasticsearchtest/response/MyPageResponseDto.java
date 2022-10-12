package com.example.elasticsearchtest.response;


import com.example.elasticsearchtest.domain.LibraryEs;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyPageResponseDto {//최근 조회한 책들을 표시하기 위한 dto
    private String bookName;
    private String authors;
    private String publisher;
    private String isbn13;
}
