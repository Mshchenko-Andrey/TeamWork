package com.skypro.teamwork.controller;

import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleListResponse;
import com.skypro.teamwork.dto.RuleStatsResponse;
import com.skypro.teamwork.service.RuleService;
import com.skypro.teamwork.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class RuleController {

    private final RuleService ruleService;
    private final StatsService statsService;

    public RuleController(RuleService ruleService, StatsService statsService) {
        this.ruleService = ruleService;
        this.statsService = statsService;
    }

    @PostMapping
    public RuleDto createRule(@RequestBody RuleDto ruleDto) {
        return ruleService.createRule(ruleDto);
    }

    @GetMapping
    public RuleListResponse getAllRules() {
        List<RuleDto> rules = ruleService.getAllRules();
        return new RuleListResponse(rules);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID productId) {
        ruleService.deleteRule(productId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/stats")
    public RuleStatsResponse getStats() {
        return new RuleStatsResponse(statsService.getAllStats());
    }
}