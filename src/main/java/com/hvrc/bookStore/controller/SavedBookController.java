package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.SavedBookDTO;
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
import java.util.stream.Collectors;

@RestController
public class SavedBookController {

   private final SavedBookService savedBookService;
   private final UserService userService;
   private final BookService bookService;

   public SavedBookController(SavedBookService savedBookService, UserService userService, BookService bookService) {
       this.savedBookService = savedBookService;
       this.userService = userService;
       this.bookService = bookService;
   }

    @GetMapping("/saved-books")
    public List<SavedBookDTO> getSavedBooksByUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());

        return savedBookService.getSavedBooksByUser(user)
                .stream()
                .map(SavedBookDTO::new)
                .collect(Collectors.toList());
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
