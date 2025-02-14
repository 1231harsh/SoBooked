package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.SavedBook;
import com.hvrc.bookStore.service.SavedBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SavedBookController {

    @Autowired
    private SavedBookService savedBookService;

    @GetMapping("/saved-books/{userId}")
    public List<SavedBook> getSavedBooksByUser(@PathVariable Long userId) {
        return savedBookService.getSavedBooksByUser(userId);
    }

    @GetMapping("/saved-book/save")
    public SavedBook saveBook(SavedBook savedBook) {
        return savedBookService.saveBook(savedBook);
    }
}
