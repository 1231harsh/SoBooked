package com.hvrc.bookStore.controller;

import com.hvrc.bookStore.entity.User;
import com.hvrc.bookStore.service.RecommendationService;
import com.hvrc.bookStore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {

    @Autowired
    private UserService userService;

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/getBooks")
    public Mono<List<Integer>> getRecommendations(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        Long userId = user.getId();
        return recommendationService.getRecommendedBooks(userId);
    }
}
