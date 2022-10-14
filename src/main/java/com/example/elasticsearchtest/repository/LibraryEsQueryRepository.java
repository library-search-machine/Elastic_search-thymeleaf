package com.example.elasticsearchtest.repository;

import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.dto.libraryRequestDto;
import lombok.RequiredArgsConstructor;

import org.elasticsearch.index.query.*;
import org.elasticsearch.search.collapse.CollapseBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchPhraseQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
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
        CollapseBuilder collapseBuilder = new CollapseBuilder("isbn13");
        Pageable pageable = PageRequest.of(0, 1000);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .must(QueryBuilders.matchQuery("bookName",keyword))//문장이 완전 같지 않아도 검색
                .should(QueryBuilders.termQuery("bookName.keyword",keyword))//완전히 일치하는 문자열
                .should(QueryBuilders.matchPhraseQuery("bookName",keyword));

        NativeSearchQuery nativeSearchQuery= new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .withCollapseBuilder(collapseBuilder)
                .build();

        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return list;
    }

    public List<LibraryEs> findByAuthors(String keyword) {
        Pageable pageable = PageRequest.of(0, 1000);
        MatchPhraseQueryBuilder matchQueryBuilder = QueryBuilders.matchPhraseQuery("authors", keyword);
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
        return list;
    }

    public List<LibraryEs> findByIsbn13(String keyword) {
        Pageable pageable = PageRequest.of(0, 1000);
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("isbn13", keyword);
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
        return list;
    }

    public List<LibraryEs> findByAll(libraryRequestDto requestDto) {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        RangeQueryBuilder yearRangeQueryBuilder = QueryBuilders.rangeQuery("publicationYear");
        RangeQueryBuilder genreRangeQueryBuilder = QueryBuilders.rangeQuery("class_num");

        Pageable pageable = PageRequest.of(0, 10000);

        if (requestDto.getBookName() != null) { //제목 상세 검색 쿼리 추가
            boolQueryBuilder.must(QueryBuilders.matchQuery("bookName", requestDto.getBookName()).operator(Operator.fromString("and")));
            boolQueryBuilder.should(QueryBuilders.matchPhraseQuery("bookName", requestDto.getBookName()));
        }
        if (requestDto.getAuthors() != null) { //저자 상세 검색 쿼리 추가
            boolQueryBuilder.must(QueryBuilders.matchQuery("authors", requestDto.getAuthors()));
        }
        if (requestDto.getPublisher() != null) { //출판사 상세 검색 쿼리 추가
            boolQueryBuilder.must(QueryBuilders.matchQuery("publisher", requestDto.getPublisher()));
        }
        if (!requestDto.getGenre().equals("전체")) { //장르 상세 검색 쿼리 추가
            int genreNum = Integer.parseInt(requestDto.getGenre());
            boolQueryBuilder.filter(genreRangeQueryBuilder.gte(genreNum).lt(genreNum+100));
        }
        if (requestDto.getLibrary() != null) { //도서관 상세 검색 쿼리 추가
            boolQueryBuilder.filter(QueryBuilders.matchPhraseQuery("libraryName", requestDto.getLibrary()));
        }

        //날짜 상세 검색 쿼리 추가
        boolQueryBuilder.filter(yearRangeQueryBuilder.gte(requestDto.getFirstPublication()).lt(requestDto.getEndPublication()).format("yyyy"));

        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();

        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return list;
    }

    public List<LibraryEs> autocomplete_book(String keyword) {

        Pageable pageable = PageRequest.of(0, 20);
        PrefixQueryBuilder prefixQueryBuilder = QueryBuilders.prefixQuery("bookName.keyword", keyword);

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(prefixQueryBuilder);
        //  .should(QueryBuilders.matchPhraseQuery("bookName",keyword));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withPageable(pageable)
                .build();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        String json = nativeSearchQuery.getQuery().toString();
        System.out.println(json);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        return list;
    }

    public List<LibraryEs> autocomplete_book2(String keyword) {
        Pageable pageable = PageRequest.of(0, 10);
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery()
                .should(QueryBuilders.matchPhraseQuery("bookName", keyword));
        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                .withQuery(boolQueryBuilder)
                .withCollapseField("isbn13")
                .withPageable(pageable)
                .build();
        SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
        List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();
        List<LibraryEs> list = new ArrayList<>();
        for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
            list.add(libraryEsSearchHit.getContent());
        }
        for (LibraryEs libraryEs : list) {
            libraryEs.getLibraryName();
        }
        return list;
    }
    public List<LibraryEs> findByIsbn13One(List<String> Isbn13List) {
        List<LibraryEs> list = new ArrayList<>();
        for (String isbn : Isbn13List) {
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("isbn13",isbn);
            Pageable pageable = PageRequest.of(0, 1);//하나만 저장을 해서 page = 1
            NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
                    .withQuery(matchQueryBuilder)
                    .withPageable(pageable)
                    .build();
            SearchHits<LibraryEs> search = operations.search(nativeSearchQuery, LibraryEs.class);
            List<SearchHit<LibraryEs>> searchHitList = search.getSearchHits();

            for (SearchHit<LibraryEs> libraryEsSearchHit : searchHitList) {
                list.add(libraryEsSearchHit.getContent());
            }
        }
        return list;
    }
}
