package com.example.elasticsearchtest.controller;



import com.example.elasticsearchtest.response.BookResponseDto;
import com.example.elasticsearchtest.response.BookResponseDto2;
import com.example.elasticsearchtest.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.net.MalformedURLException;

@RequiredArgsConstructor
@Controller
public class BookController {

    private final BookService bookService;


    @GetMapping("/")
    public String main(Model model){
        return "main";
    }
    @GetMapping("/search")
    public String getBook(@RequestParam() String keyword,@RequestParam() String type,@RequestParam() int page,Model model){
        Page<BookResponseDto> bookList =  bookService.getBook(keyword,type,page);
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
        model.addAttribute("endCount", endCount);
        if (page / 10 < 1) {
            startIndex=1;
            System.out.println("startIndex = " + startIndex);
            endIndex=10;
            if(endIndex>=bookList.getTotalPages())
                endIndex= bookList.getTotalPages();
        } else  {
            if(page%10==0){
                page-=1;
            }
            System.out.println("page = " + page);
            startIndex=page/10*10+1;
            endIndex=startIndex+9;
            if(endIndex>=bookList.getTotalPages())
                endIndex= bookList.getTotalPages();
        }

        model.addAttribute("startIndex", startIndex);
        model.addAttribute("endIndex", endIndex);


        //지금 필요한거 지금 total pae

        model.addAttribute("bookList", bookList);
        model.addAttribute("keyword", keyword);

        return "main";
    }
    @GetMapping("/search_isbn")
    public String getBookIsbn(@RequestParam() String isbn,Model model) throws MalformedURLException {
        //isbn으로 호출을하고 그 책들의 정보들을 호출해 주고 그리고 또 뭐냐 full text index써서 해당 도서관리스트를 리턴을 하자
        BookResponseDto2 bookByIsbn = bookService.getBookByIsbn(isbn);
        model.addAttribute("bookname",bookByIsbn.getBookName());
        model.addAttribute("authors",bookByIsbn.getAuthors());
        model.addAttribute("publisher",bookByIsbn.getPublisher());
        model.addAttribute("publicationYear",bookByIsbn.getPublicationYear());
        model.addAttribute("bookImageURL",bookByIsbn.getBookImageURL());
        model.addAttribute("description",bookByIsbn.getDescription());
        model.addAttribute("class_nm",bookByIsbn.getClass_nm());
        model.addAttribute("LibraryList",bookByIsbn.getLibraryList());
        return "detail";
    }




}
