package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.service.BookRecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookRecommendationController {

    @Autowired
    private BookRecommendationService bookRecommendationService;

    @GetMapping("/api/recommend")
    public ResponseEntity<List<BookDTO>> getRecommendations(@RequestParam String bookName) {
        List<BookDTO> recommendations = bookRecommendationService.getBookRecommendations(bookName);
        recommendations.forEach(System.out::println);
        return ResponseEntity.ok(recommendations);
    }
}
