package com.hvrc.bookStore.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hvrc.bookStore.dto.BookDTO;
import com.hvrc.bookStore.model.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookRecommendationService {

    private final RestTemplate restTemplate;

    @Autowired
    private BookService bookService;

    public BookRecommendationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<BookDTO> getBookRecommendations(String bookName) {

        String flaskApiUrl = "http://localhost:5000/recommend?book_name=" + bookName;

        ResponseEntity<String> response = restTemplate.getForEntity(flaskApiUrl, String.class);
        String jsonResponse = response.getBody();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            List<String> recommendations = objectMapper.readValue(jsonResponse, new TypeReference<List<String>>() {});
            List<BookDTO> bookRecommendations = new ArrayList<>();
            for (int i=0;i<recommendations.size();i++){
                BookDTO book= bookService.getBook(recommendations.get(i));
                bookRecommendations.add(book);
            }
            return bookRecommendations;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }

    }
}
