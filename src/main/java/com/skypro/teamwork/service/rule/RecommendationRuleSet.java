package com.skypro.teamwork.service.rule;

import com.skypro.teamwork.dto.RecommendationDto;
import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {
    Optional<RecommendationDto> check(UUID userId);
}