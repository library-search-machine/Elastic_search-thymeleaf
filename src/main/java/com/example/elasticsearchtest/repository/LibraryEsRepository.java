package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.LibraryEs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import org.springframework.stereotype.Repository;

import java.util.List;




    @Repository
    public interface LibraryEsRepository extends ElasticsearchRepository<LibraryEs, Long> {
        Page<LibraryEs> findByBookName(String bookname, Pageable pageable);
        Page<LibraryEs> findByAuthors(String authors, Pageable pageable);

        Page<LibraryEs> findByIsbn13(String isbn, Pageable pageable);
        @Query("{\"match\": {\"isbn13\": {\"query\": \"?0\"}}}")//쿼리 문으로 처리
        List<LibraryEs> findByIsbn13All(String isbn);






}
