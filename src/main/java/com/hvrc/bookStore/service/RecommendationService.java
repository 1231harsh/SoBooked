package com.hvrc.bookStore.service;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Map;

@Service
public class RecommendationService {

    private final WebClient webClient;

    public RecommendationService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://127.0.0.1:5000").build();
        this.webClient = webClientBuilder.baseUrl("https://reccomendation-system.onrender.com").build();
    }

    public Mono<List<Integer>> getRecommendedBooks(Long userId) {
        return webClient.get()
                .uri("/recommend?user_id=" + userId)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                .map(response -> {
                    if (response != null && response.get("recommended_books") instanceof List<?>) {
                        return ((List<?>) response.get("recommended_books"))
                                .stream()
                                .filter(item -> item instanceof Integer)
                                .map(item -> (Integer) item)
                                .toList();
                    } else {
                        return List.of();
                    }
                });
    }
}
