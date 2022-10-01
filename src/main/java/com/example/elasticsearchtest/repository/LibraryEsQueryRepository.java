package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.dto.libraryRequestDto;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.*;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class LibraryEsQueryRepository {

    private final ElasticsearchOperations operations;
    public List<LibraryEs> findByBookName(String keyword) {
        Pageable pageable = PageRequest.of(0, 1000);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("bookName",keyword))//문장이 완전 같지 않아도 검색
                .should(QueryBuilders.termQuery("bookName.keyword",keyword))//완전히 일치하는 문자열
                .should(QueryBuilders.matchPhraseQuery("bookName",keyword));//token값들을 가져오고 그 토큰들의 순서대로 검색해서 나온 검색값 return
        NativeSearchQuery nativeSearchQuery= new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return  list;
    }
    public List<LibraryEs> findByAuthors(String keyword) {
        Pageable pageable = PageRequest.of(0, 1000);
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("authors",keyword);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .build();

        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }


        return  list;
    }
    public List<LibraryEs> findByIsbn13(String keyword) {
        Pageable pageable = PageRequest.of(0, 1000);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("isbn13",keyword);
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQueryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return  list;
    }

    public List<LibraryEs> findByAll(libraryRequestDto requestDto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        Pageable pageable = PageRequest.of(0, 1000);
        if(requestDto.getBookName() != null){
            boolQueryBuilder.must(QueryBuilders.matchQuery("bookName",requestDto.getBookName()).operator(Operator.fromString("and")));
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("bookName",requestDto.getBookName()));
        }
        if (requestDto.getAuthors() != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("authors", requestDto.getAuthors()));
        }
        if (requestDto.getPublisher() != null) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("publisher", requestDto.getPublisher()));
        }
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
        System.out.println(nativeSearchQuery.getQuery().toString());
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return  list;
    }


}
