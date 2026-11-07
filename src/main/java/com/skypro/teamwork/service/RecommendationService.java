package com.skypro.teamwork.service;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.service.rule.RecommendationRuleSet;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;
    private final DynamicRuleService dynamicRuleService;

    public RecommendationService(List<RecommendationRuleSet> ruleSets, DynamicRuleService dynamicRuleService) {
        this.ruleSets = ruleSets;
        this.dynamicRuleService = dynamicRuleService;
    }

    public List<RecommendationDto> getRecommendations(UUID userId) {
        List<RecommendationDto> recommendations = new ArrayList<>();


        for (RecommendationRuleSet ruleSet : ruleSets) {
            Optional<RecommendationDto> recommendation = ruleSet.check(userId);
            recommendation.ifPresent(recommendations::add);
        }


        dynamicRuleService.checkDynamicRules(userId).ifPresent(recommendations::add);

        return recommendations;
    }
}