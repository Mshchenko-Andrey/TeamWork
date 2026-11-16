package com.skypro.teamwork.controller;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.dto.RecommendationResponse;
import com.skypro.teamwork.service.RecommendationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

/**
 * REST-контроллер для получения рекомендаций пользователя.
 */
@RestController
@Tag(name = "Рекомендации", description = "API для получения персональных рекомендаций пользователя")
public class RecommendationController {

    private final RecommendationService service;

    public RecommendationController(RecommendationService service) {
        this.service = service;
    }

    /**
     * Возвращает список рекомендованных продуктов для указанного пользователя.
     *
     * @param userId ID пользователя (UUID).
     * @return JSON с ID пользователя и списком рекомендаций.
     */
    @GetMapping("/recommendation/{userId}")
    @Operation(summary = "Получить рекомендации для пользователя", description = "Возвращает список рекомендованных банковских продуктов для указанного пользователя (по его UUID).")
    public RecommendationResponse getRecommendations(@PathVariable UUID userId) {
        List<RecommendationDto> recommendations = service.getRecommendations(userId);
        return new RecommendationResponse(userId, recommendations);
    }
}