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

    @Autowired
    private UserBookActivityService userBookActivityService;

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BookService bookService;
    @GetMapping("/api/user-activity")
    public List<UserBookActivity> getUserActivities() {
        System.out.println("Flask hit kia!!!!");
        userBookActivityService.getUserActivities().forEach(System.out::println);
        return userBookActivityService.getUserActivities();
    }

    @PostMapping("/api/user-activity/save")
    public ResponseEntity<String> save(Principal principal, @RequestBody String jsonPayload) {
        try {
            UserBookActivityDTO userBookActivityDTO = objectMapper.readValue(jsonPayload, UserBookActivityDTO.class);
            User user = userService.findByUsername(principal.getName());
            Book book = bookService.getBookById(userBookActivityDTO.getBookId());

            UserBookActivity userBookActivity = new UserBookActivity(user, book, userBookActivityDTO.getActionType());

            userBookActivityService.save(userBookActivity);
            return ResponseEntity.status(HttpStatus.CREATED).body("Activity saved successfully.");
        } catch (Exception e) {
             return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid request: " + e.getMessage());
        }
    }
}
