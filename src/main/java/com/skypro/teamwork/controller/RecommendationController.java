package com.skypro.teamwork.controller;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.dto.RecommendationResponse;
import com.skypro.teamwork.service.RecommendationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    @GetMapping("/recommendation/{userId}")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDto> recommendations = service.getRecommendations(userId);
        return new RecommendationResponse(userId, recommendations);
    }
}