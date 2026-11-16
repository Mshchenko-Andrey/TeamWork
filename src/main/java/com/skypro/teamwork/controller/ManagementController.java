package com.skypro.teamwork.controller;

import com.skypro.teamwork.repository.RecommendationsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * REST-контроллер для служебных эндпоинтов (управление сервисом).
 */
@RestController
@RequestMapping("/management")
@Tag(name = "Управление сервисом", description = "Служебные эндпоинты для мониторинга и сброса кеша")
public class ManagementController {

    private final RecommendationsRepository recommendationsRepository;
    private final BuildProperties buildProperties;

    public ManagementController(RecommendationsRepository recommendationsRepository,
                                BuildProperties buildProperties) {
        this.recommendationsRepository = recommendationsRepository;
        this.buildProperties = buildProperties;
    }

    /**
     * Очищает все кеши приложения.
     */
    @PostMapping("/clear-caches")
    @Operation(summary = "Очистить кеши", description = "Полностью очищает все кеши Caffeine, чтобы при следующем запросе данные брались заново из базы данных.")
    public ResponseEntity<Void> clearCaches() {
        recommendationsRepository.clearAllCaches();
        return ResponseEntity.ok().build();
    }

    /**
     * Возвращает информацию о сервисе (название и версию).
     */
    @GetMapping("/info")
    @Operation(summary = "Информация о сервисе", description = "Возвращает название и версию сервиса, взятые из файла конфигурации pom.xml.")
    public ResponseEntity<Map<String, String>> getInfo() {
        return ResponseEntity.ok(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}