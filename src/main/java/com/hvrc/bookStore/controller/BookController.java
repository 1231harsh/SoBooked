package com.hvrc.bookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class BookController {
    @Autowired
    private BookService bookService;

    @PostMapping("/api/test")
    public String test() {
        return "Test";
    }

    @PostMapping("/api/add")
    public boolean addBook(@RequestParam("book") String bookJson, @RequestParam("file") MultipartFile file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Book book = objectMapper.readValue(bookJson, Book.class);



        book.setPhoto(file.getBytes());

        return bookService.save(book);
    }

    @GetMapping("/api/city/{city}")
    public List<Book> getBooksByCity(@PathVariable String city) {
        return bookService.getBooksByCity(city);
    }

    @GetMapping("/api/rent/city/{city}")
    public List<Book> getBooksForRentByCity(@PathVariable String city) {
        return bookService.getBooksForRentByCity(city);
    }

    @GetMapping("/api/getBooks")
    public ResponseEntity<List<BookDTO>> getBooks() {
        List<BookDTO> books= bookService.getBooks();
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @DeleteMapping("/api/sell/{id}")
    public String sellBook(@PathVariable Long id) {
        bookService.sellBook(id);
        return "Book sold successfully";
    }

    @PutMapping("/api/rent/{id}")
    public String rentBook(@PathVariable Long id) {
        bookService.rentBook(id);
        return "Book rented successfully";
    }

}
