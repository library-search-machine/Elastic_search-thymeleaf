package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.BooksReview;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MongodbRepository extends MongoRepository<BooksReview,String> {

    List<BooksReview> findByIsbn13(String isbn13);
    List<BooksReview> findByNickname(String nickname);

}
