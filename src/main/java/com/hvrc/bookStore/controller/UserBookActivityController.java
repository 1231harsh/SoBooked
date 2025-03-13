package com.hvrc.bookStore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvrc.bookStore.dto.UserBookActivityDTO;
import com.hvrc.bookStore.entity.Book;
import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.entity.UserBookActivity;
import com.hvrc.bookStore.service.BookService;
import com.hvrc.bookStore.service.UserBookActivityService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
public class UserBookActivityController {

    private final UserBookActivityService userBookActivityService;
    private final UserService userService;
    private final BookService bookService;

    public UserBookActivityController(UserBookActivityService userBookActivityService, UserService userService, BookService bookService) {
        this.userBookActivityService = userBookActivityService;
        this.userService = userService;
        this.bookService = bookService;
    }

    @GetMapping("/api/user-activity")
    public List<UserBookActivityDTO> getUserActivities() {
        System.out.println("Flask hit kia!!!!");
        return userBookActivityService.getUserActivities()
                .stream()
                .map(UserBookActivityDTO::new)
                .toList();
    }


    @PostMapping("/api/user-activity/save")
    public ResponseEntity<String> save(Principal principal, @RequestBody UserBookActivityDTO userBookActivityDTO) {
        try {
            User user = userService.findByUsername(principal.getName());
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
            }

            Book book = bookService.getBookById(userBookActivityDTO.getBookId());
            if (book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book not found.");
            }

            UserBookActivity userBookActivity = new UserBookActivity(user, book, userBookActivityDTO.getActionType());

            userBookActivityService.save(userBookActivity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Activity saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        }
    }
}
