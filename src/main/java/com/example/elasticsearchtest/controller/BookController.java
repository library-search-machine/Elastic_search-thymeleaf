package com.example.elasticsearchtest.controller;


import com.example.elasticsearchtest.domain.BooksReview;
import com.example.elasticsearchtest.response.BookResponseDto;
import com.example.elasticsearchtest.response.BookResponseDto2;
import com.example.elasticsearchtest.response.BookResponseDto3;
import com.example.elasticsearchtest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String main(Model model, HttpServletRequest request,HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();//전체 쿠키를 받는 애 여기서 isbn만 뽑아와야할듯합니다.
        if(cookies!=null) {
            StringBuilder sb = new StringBuilder();//지금 저장된 토큰을 통해 최근 조회한 isbn을
            for (int i = 0; i < cookies.length; i++) {
                if (i == cookies.length - 1)
                    sb.append(cookies[i].getValue());
                else {
                    sb.append(cookies[i].getValue());
                    sb.append(";");
                }
            }
            List<BookResponseDto3> booksList = bookService.recommend_Book(sb.toString());
            model.addAttribute("list", booksList);
            return "main";
        }
        return "main";
    }
    @GetMapping("/search")
    public String getBook(@RequestParam() String keyword, @RequestParam() String type, @RequestParam() int page, Model model, HttpServletRequest request, HttpServletResponse response) {
        Page<BookResponseDto> bookList = bookService.getBook(keyword, type, page);
        model.addAttribute("totalPages", bookList.getTotalPages());
        model.addAttribute("totalItems", bookList.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("type", type);
        model.addAttribute("current_page", page);
        int startIndex;
        int endIndex;
        long startCount = (page - 1) * 30 + 1;
        long endCount = startCount + 30 - 1;
        model.addAttribute("startCount", startCount);
        if (page / 10 < 1) {
            startIndex = 1;
            endIndex = 10;
            if (endIndex >= bookList.getTotalPages())
                endIndex = bookList.getTotalPages();
        } else {
            if (page % 10 == 0) {
                page -= 1;
            }
            startIndex = page / 10 * 10 + 1;
            endIndex = startIndex + 9;
            if (endIndex >= bookList.getTotalPages())
                endIndex = bookList.getTotalPages();
        }

        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);


        if (endCount > bookList.getTotalElements())
            model.addAttribute("endCount", bookList.getTotalElements());
        else
            model.addAttribute("endCount", endCount);


        model.addAttribute("bookList", bookList);
        model.addAttribute("keyword", keyword);

        return "main";
    }

    @GetMapping("/search_isbn")
    public String getBookIsbn(@RequestParam() String isbn, Model model, HttpServletResponse response,HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();//전체 쿠키를 받는 애 여기서 isbn만 뽑아와야할듯합니다.
        if(cookies!=null){
            if (cookies.length == 3) {
                Cookie deleteCookie = cookies[0];//가장 처음 저장된 쿠키를 삭제
                deleteCookie.setMaxAge(0);
                response.addCookie( deleteCookie);
            }
        }
        Cookie cookie = new Cookie(isbn, isbn);
        cookie.setMaxAge(600);
        response.addCookie(cookie);//새로운 쿠키를 저장
        double score =0;
        String total;
        //isbn으로 호출을하고 그 책들의 정보들을 호출해 주고 그리고 또 뭐냐 full text index써서 해당 도서관리스트를 리턴을 하자
        BookResponseDto2 bookByIsbn = bookService.getBookByIsbn(isbn);
        for (BooksReview booksReview : bookByIsbn.getBooksReviewList()) {
            score+= booksReview.getStars();
        }
        if(bookByIsbn.getBooksReviewList().size()==0){
            total="별점이 아직 등록되지 않았습니다😅";
        }
        else{
            score=score/bookByIsbn.getBooksReviewList().size();
            total= String.format("%.2f",score);
        }

        model.addAttribute("bookname", bookByIsbn.getBookName());
        model.addAttribute("total",total);
        model.addAttribute("authors", bookByIsbn.getAuthors());
        model.addAttribute("publisher", bookByIsbn.getPublisher());
        model.addAttribute("publicationYear", bookByIsbn.getPublicationYear());
        model.addAttribute("bookImageURL", bookByIsbn.getBookImageURL());
        model.addAttribute("description", bookByIsbn.getDescription());
        model.addAttribute("class_nm", bookByIsbn.getClass_nm());
        model.addAttribute("class_no", bookByIsbn.getClass_no());
        model.addAttribute("BooksReviewList",bookByIsbn.getBooksReviewList());
        model.addAttribute("LibraryList", bookByIsbn.getLibraryList());
        return "detail";
    }


}
