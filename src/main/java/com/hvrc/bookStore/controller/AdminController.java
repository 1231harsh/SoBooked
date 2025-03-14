package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/books")
public class AdminController {

    private final BookService bookService;

    public AdminController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/addBook")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        bookService.save(book);
        return ResponseEntity.ok("Book added successfully");
    }

    @PutMapping("/updateBook/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> updateBook(@PathVariable Long bookId, @RequestBody Book updatedBook) {
        bookService.updateBook(bookId, updatedBook);
        return ResponseEntity.ok("Book updated successfully");
    }

    @DeleteMapping("/deleteBook/{bookId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<String> deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return ResponseEntity.ok("Book deleted successfully");
    }
}
