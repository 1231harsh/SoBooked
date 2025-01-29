package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @GetMapping("/api/getBooks")
    public ResponseEntity<List<BookDTO>> getBooks() {
        List<BookDTO> books= bookService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }
}
