package com.skypro.teamwork.controller;

import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleListResponse;
import com.skypro.teamwork.dto.RuleStatsResponse;
import com.skypro.teamwork.service.RuleService;
import com.skypro.teamwork.service.StatsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST-контроллер для управления динамическими правилами рекомендаций.
 */
@RestController
@RequestMapping("/rule")
@Tag(name = "Управление правилами", description = "API для создания, просмотра и удаления динамических правил рекомендаций")
public class RuleController {

    private final RuleService ruleService;
    private final StatsService statsService;

    public RuleController(RuleService ruleService, StatsService statsService) {
        this.ruleService = ruleService;
        this.statsService = statsService;
    }

    /**
     * Создает новое динамическое правило.
     */
    @PostMapping
    @Operation(summary = "Создать новое правило", description = "Принимает JSON с продуктом и списком условий, сохраняет в базу и возвращает созданное правило с присвоенным ID.")
    public RuleDto createRule(@RequestBody RuleDto ruleDto) {
        return ruleService.createRule(ruleDto);
    }

    /**
     * Возвращает список всех динамических правил.
     */
    @GetMapping
    @Operation(summary = "Получить все правила", description = "Возвращает список всех динамических правил, сохраненных в базе данных.")
    public RuleListResponse getAllRules() {
        List<RuleDto> rules = ruleService.getAllRules();
        return new RuleListResponse(rules);
    }

    /**
     * Удаляет правило по ID продукта.
     */
    @DeleteMapping("/{productId}")
    @Operation(summary = "Удалить правило по ID продукта", description = "Удаляет динамическое правило из базы данных. Возвращает 204 No Content при успехе.")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        ruleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Возвращает статистику срабатывания всех правил.
     */
    @GetMapping("/stats")
    @Operation(summary = "Получить статистику срабатывания правил", description = "Возвращает список всех правил и количество раз, которое каждое правило срабатывало для пользователей.")
    public RuleStatsResponse getStats() {
        return new RuleStatsResponse(statsService.getAllStats());
    }
}