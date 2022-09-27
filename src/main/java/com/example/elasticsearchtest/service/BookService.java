package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.repository.LibraryEsRepository;
import com.example.elasticsearchtest.response.BookResponseDto;
import com.example.elasticsearchtest.response.BookResponseDto2;
import lombok.RequiredArgsConstructor;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Book;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class BookService {

    private final LibraryEsRepository libraryEsRepository;

    @Transactional
    public Page<BookResponseDto> getBook(String keyword, String type, int page) {

        Pageable pageable = PageRequest.of(page - 1, 30);
        Page<LibraryEs> bookList;
        long beforeTime = System.currentTimeMillis(); //코드 실행 전에 시간 받아오기
        if (type.equals("title"))
            bookList = libraryEsRepository.findByBookName(keyword, pageable);

        else if (type.equals("isbn"))
            bookList = libraryEsRepository.findByIsbn13(keyword, pageable);

        else
            bookList = libraryEsRepository.findByAuthors(keyword, pageable);


        Page<BookResponseDto> bookResponseDtoList = new BookResponseDto().toDtoList(bookList);

        long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
        long secDiffTime = (afterTime - beforeTime);
        System.out.println("시간차이(m) : using full text search" + secDiffTime);
        return bookResponseDtoList;
    }


//    @Transactional
//    public Page<BookResponseDto> query_test(String keyword, String type, int page) {
//        PageRequest pageRequest = PageRequest.of(0, 50);
//        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("bookName", "keyword");
//
//        NativeSearchQuery nativeSearchQuery = new NativeSearchQueryBuilder()
//                .withQuery(matchQueryBuilder)
//                .withPageable(pageRequest)
//                .build();
//        String query = nativeSearchQuery.getQuery().toString();
//
//        System.out.println("query = " + query);
//
//        LibraryEsRepository.search()
//
//    }


    @Transactional
    public BookResponseDto2 getBookByIsbn(String isbn) throws MalformedURLException {
        List<LibraryEs> LibraryList = libraryEsRepository.findByIsbn13All(isbn);


        Set<String> LibraryList2 = new HashSet<>(); //중복값을 제거하기 위해 set채용
        for (LibraryEs libraryEs : LibraryList) {
            LibraryList2.add(libraryEs.getLibraryName());
        }
        //도서나루 open api를 통해 도서 상세 정보를 불러오는 부분
        String url_address = "http://data4library.kr/api/srchDtlList?authKey=6bd363e870bb744d2e52c35f15cfef0aa929faba70bc2d66961aae91e101901f&isbn13=" + isbn + "&loaninfoYN=N&format=json";
        try {
            URL url = new URL(url_address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();
            JSONParser parser = new JSONParser();
            JSONObject obj = (JSONObject) parser.parse(sb.toString());
            JSONObject response = (JSONObject) obj.get("response");
            JSONArray detail = (JSONArray) response.get("detail");
            JSONObject temp = (JSONObject) detail.get(0);
            JSONObject book = (JSONObject) temp.get("book");

            String description = (String) book.get("description");
            description = description.replaceAll("&gt;", "");
            description = description.replaceAll("&lt;", "");
            BookResponseDto2 bookResponseDto = BookResponseDto2.builder()
                    .bookName((String) book.get("bookname"))
                    .authors((String) book.get("authors"))
                    .publisher((String) book.get("publisher"))
                    .class_nm((String) book.get("class_nm"))
                    .publicationYear((String) book.get("publication_year"))
                    .bookImageURL((String) book.get("bookImageURL"))
                    .class_no((String) book.get("class_no"))
                    .description(description)
                    .LibraryList(LibraryList2)
                    .build();
            return bookResponseDto;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
