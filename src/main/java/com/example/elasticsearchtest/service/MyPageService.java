package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.repository.LibraryEsQueryRepository;
import com.example.elasticsearchtest.repository.MongodbRepository;
import com.example.elasticsearchtest.response.BookReviewResponse;
import com.example.elasticsearchtest.response.MyPageResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final MongodbRepository mongodbRepository;
    private final LibraryEsQueryRepository libraryEsQueryRepository;
    @Transactional
    public List<MyPageResponseDto> test(List<String> isbn) {
        List<LibraryEs> byIsbn13One = libraryEsQueryRepository.findByIsbn13One(isbn);//하나씩 저장을 해주는 거임 isbneofh
        List<MyPageResponseDto> booklist =new ArrayList<>();
        for (LibraryEs libraryEs : byIsbn13One) {
            MyPageResponseDto myPageResponseDto =MyPageResponseDto.builder()
                    .publisher(libraryEs.getPublisher())
                    .bookName(libraryEs.getBookName())
                    .authors(libraryEs.getAuthors())
                    .bookName(libraryEs.getBookName())
                    .isbn13(libraryEs.getIsbn13())
                    .build();
            booklist.add(myPageResponseDto);
        }
        return booklist;
    }

    @Transactional
    public List<BookReviewResponse> findByNickname(String nickname) {
        List<BooksReview> commentList = mongodbRepository.findByNickname(nickname);
        List<BookReviewResponse> commentDtoList  = new ArrayList<>();
        for (BooksReview booksReview : commentList) {
            BookReviewResponse bookReviewResponse = BookReviewResponse.builder()
                    .id(booksReview.getId())
                    .title(booksReview.getTitle())
                    .comment(booksReview.getComment())
                    .stars(booksReview.getStars())
                    .isbn13(booksReview.getIsbn13())
                    .nickname(booksReview.getNickname())
                    .bookName(booksReview.getBookName())
                    .build();
            commentDtoList.add(bookReviewResponse);
        }

        return commentDtoList;
    }

}
