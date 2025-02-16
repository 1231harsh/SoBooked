package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.SavedBook;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.BookService;
import com.hvrc.bookStore.service.SavedBookService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class SavedBookController {

    @Autowired
    private UserService userService;
    @Autowired
    private SavedBookService savedBookService;

    @Autowired
    private BookService bookService;

    @GetMapping("/saved-books/{userId}")
    public List<SavedBook> getSavedBooksByUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return savedBookService.getSavedBooksByUser(user);
    }

    @PostMapping("/saved-book/save")
    public SavedBook saveBook(Principal principal, @RequestParam Long bookId) {
        User user = userService.findByUsername(principal.getName());
        Book book = bookService.getBookById(bookId);
        SavedBook savedBook = new SavedBook();
        savedBook.setUser(user);
        savedBook.setBook(book);
        savedBook.setLiked(true);
        return savedBookService.saveBook(savedBook);
    }

    @PostMapping("/saved-book/unsave")
    public void unsaveBook(Principal principal, @RequestParam Long bookId) {
        User user = userService.findByUsername(principal.getName());
        Book book = bookService.getBookById(bookId);
        savedBookService.unsaveBook(user, book);
    }
}
