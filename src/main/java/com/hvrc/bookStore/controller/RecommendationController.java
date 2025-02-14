package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.service.RecommendationService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public Mono<List<Integer>> getRecommendations(@PathVariable Long userId) {
        return recommendationService.getRecommendedBooks(userId);
    }
}
