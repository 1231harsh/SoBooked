package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
<<<<<<< HEAD
=======
import org.springframework.web.bind.annotation.PathVariable;
>>>>>>> f00bc12 (Added new files to the project)
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
<<<<<<< HEAD
=======

    @GetMapping("/api/book/{bookName}")
    public ResponseEntity<BookDTO> getBook(@PathVariable String bookName) {
        BookDTO book= bookService.getBook(bookName);
        return new ResponseEntity<>(book,HttpStatus.OK);
    }
>>>>>>> f00bc12 (Added new files to the project)
}
