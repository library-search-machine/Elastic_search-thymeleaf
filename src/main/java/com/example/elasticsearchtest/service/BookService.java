package com.example.elasticsearchtest.service;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.domain.LibraryEs;
import com.example.elasticsearchtest.dto.libraryRequestDto;
import com.example.elasticsearchtest.repository.LibraryEsQueryRepository;
import com.example.elasticsearchtest.repository.LibraryEsRepository;
import com.example.elasticsearchtest.dto.Response.BookResponseDto;
import com.example.elasticsearchtest.dto.Response.BookResponseDto2;
import com.example.elasticsearchtest.dto.Response.BookResponseDto3;
import com.example.elasticsearchtest.repository.MongodbRepository;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final LibraryEsQueryRepository libraryEsQueryRepository;
    private final LibraryEsRepository libraryEsRepository;
    private final MongodbRepository mongodbRepository;

    @Transactional
    public Page<BookResponseDto> getBook(String keyword, String type, int page) {
        Pageable pageable = PageRequest.of(page - 1, 30);
        List<LibraryEs> bookList;
        switch (type) {
            case "authors":
                bookList = libraryEsQueryRepository.findByAuthors(keyword);
                break;
            case "isbn":
                bookList = libraryEsQueryRepository.findByIsbn13(keyword);
                break;
            case "advanced":
                String[] keywordArray = keyword.split("-");
                libraryRequestDto requestDto = libraryRequestDto.builder()
                        .bookName(!keywordArray[0].equals("@") ? keywordArray[0] : null)
                        .authors(!keywordArray[1].equals("@") ? keywordArray[1] : null)
                        .publisher(!keywordArray[2].equals("@") ? keywordArray[2] : null)
                        .build();

                bookList = libraryEsQueryRepository.findByAll(requestDto);
                break;
            case "title":
            default:
                bookList = libraryEsQueryRepository.findByBookName(keyword);
                break;
        }
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), bookList.size());
        return BookResponseDto.toDtoList(new PageImpl<>(bookList.subList(start, end), pageable, bookList.size()));
    }

    public Page<BookResponseDto> getBook(libraryRequestDto requestDto,  int page) {
        Pageable pageable = PageRequest.of(page - 1, 30);
        List<LibraryEs> bookList;

        bookList = libraryEsQueryRepository.findByAll(requestDto);

        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), bookList.size());
        return BookResponseDto.toDtoList(new PageImpl<>(bookList.subList(start, end), pageable, bookList.size()));
    }

    @Transactional
    public List<String> autocomplete_book(String keyword) {
        List<LibraryEs> list = libraryEsQueryRepository.autocomplete_book(keyword);
        list.addAll(libraryEsQueryRepository.autocomplete_book2(keyword));
        list = deduplication((ArrayList<LibraryEs>) list, LibraryEs::getBookName);//????????? ?????? ????????????
        List<String> bookNames = new ArrayList<>();
        for (LibraryEs libraryEs : list) {
            bookNames.add(libraryEs.getBookName());
        }
        return bookNames;
    }

    @Transactional
    public BookResponseDto2 getBookByIsbn(String isbn) {
        List<LibraryEs> LibraryList = libraryEsRepository.findByIsbn13All(isbn);
        Set<String> LibraryList2 = new HashSet<>(); //???????????? ???????????? ?????? set??????
        for (LibraryEs libraryEs : LibraryList) {
            LibraryList2.add(libraryEs.getLibraryName());
        }
        //???????????? open api??? ?????? ?????? ?????? ????????? ???????????? ??????
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
                    .booksReviewList(mongodbRepository.findByIsbn13(isbn)) //???????????? ???????????? ?????????..
                    .LibraryList(LibraryList2)
                    .build();
            return bookResponseDto;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    @Transactional
    public List<BookResponseDto3>recommend_Book(String isbn) {
        //???????????? open api??? ?????? ?????? ?????? ????????? ???????????? ??????
        String url_address = "https://data4library.kr/api/recommandList?authKey=6bd363e870bb744d2e52c35f15cfef0aa929faba70bc2d66961aae91e101901f&isbn13=" + isbn + "&format=json";
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
            JSONArray detail = (JSONArray) response.get("docs");
            List<BookResponseDto3> list= new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                JSONObject temp = (JSONObject) detail.get(i);
                JSONObject book = (JSONObject) temp.get("book");
                BookResponseDto3 bookResponseDto = BookResponseDto3.builder()
                        .bookName((String) book.get("bookname"))
                        .authors((String) book.get("authors"))
                        .publisher((String) book.get("publisher"))
                        .class_nm((String) book.get("class_nm"))
                        .publicationYear((String) book.get("publication_year"))
                        .bookImageURL((String) book.get("bookImageURL"))
                        .class_no((String) book.get("class_no"))
                        .isbn13((String) book.get("isbn13"))
                        .build();
               list.add(bookResponseDto);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public <T> List<T> deduplication(ArrayList<T> list, Function<? super T, ?> key) {
        return list.stream().filter(deduplication(key)).collect(Collectors.toList());
    }
    public <T> Predicate<T> deduplication(Function<? super T, ?> key) {
        Set<Object> set = ConcurrentHashMap.newKeySet();
        return predicate -> set.add(key.apply(predicate));
    }
}
