package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.LibraryEs;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LibraryEsRepository extends ElasticsearchRepository<LibraryEs,String> {
    @Query("{\"match\": {\"isbn13\": {\"query\": \"?0\"}   }}")
//쿼리 문으로 처리
    List<LibraryEs> findByIsbn13All(String isbn);
}