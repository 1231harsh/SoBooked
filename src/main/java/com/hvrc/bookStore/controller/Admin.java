package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.model.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Admin {
    @Autowired
    private BookService bookService;

    @GetMapping("/admin/home")
    public ResponseEntity<String> home() {
//        System.out.println("inside admin home");
        return ResponseEntity.ok("Welcome Admin");
    }
    @PostMapping("/admin/addBook")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookService.save(book);
        System.out.println("inside admin addBook");
        return ResponseEntity.ok("Book added successfully");
    }

}
