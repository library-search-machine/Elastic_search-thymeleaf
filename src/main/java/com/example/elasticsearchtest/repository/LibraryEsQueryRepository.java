package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.LibraryEs;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.SearchPage;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

@Repository
@Primary
@RequiredArgsConstructor
public class LibraryEsQueryRepository {

    private final ElasticsearchOperations operations;
    public Page<LibraryEs> findByBookName(Pageable pageable,String keyword) {
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("bookName",keyword);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        SearchPage<LibraryEs> searchHits = SearchHitSupport.searchPageFor(search, pageable);
        Page<LibraryEs> page = (Page)SearchHitSupport.unwrapSearchHits(searchHits);

        return  page;
    }
    public Page<LibraryEs> findByAuthors(Pageable pageable,String keyword) {
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("authors",keyword);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .build();

        String query = nativeSearchQuery.getQuery().toString();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        SearchPage<LibraryEs> searchHits = SearchHitSupport.searchPageFor(search, pageable);
        Page<LibraryEs> page = (Page)SearchHitSupport.unwrapSearchHits(searchHits);
        return  page;
    }
    public Page<LibraryEs> findByIsbn13(Pageable pageable,String keyword) {
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("isbn13",keyword);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .build();
        String query = nativeSearchQuery.getQuery().toString();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        SearchPage<LibraryEs> searchHits = SearchHitSupport.searchPageFor(search, pageable);
        Page<LibraryEs> page = (Page)SearchHitSupport.unwrapSearchHits(searchHits);
        return  page;
    }


}
