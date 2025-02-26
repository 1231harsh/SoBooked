package com.hvrc.bookStore.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
public class UserBookActivity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private String actionType; // "VIEW", "RENT", "BUY"
    private LocalDateTime timestamp;


    public UserBookActivity() {
    }

    public UserBookActivity(User user, Book book, String actionType) {
        this.user = user;
        this.book = book;
        this.actionType = actionType;
    }
}
