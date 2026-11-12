package com.skypro.teamwork.service;

import com.skypro.teamwork.dto.RuleStatsDto;
import com.skypro.teamwork.entity.RuleStatsEntity;
import com.skypro.teamwork.repository.RuleRepository;
import com.skypro.teamwork.repository.RuleStatsRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StatsService {

    private final RuleStatsRepository ruleStatsRepository;
    private final RuleRepository ruleRepository;

    public StatsService(RuleStatsRepository ruleStatsRepository, RuleRepository ruleRepository) {
        this.ruleStatsRepository = ruleStatsRepository;
        this.ruleRepository = ruleRepository;
    }

    @Transactional
    public void incrementRuleCounter(Long ruleId) {
        RuleStatsEntity stats = ruleStatsRepository.findByRuleId(ruleId)
                .orElseGet(() -> {
                    RuleStatsEntity newStats = new RuleStatsEntity();
                    newStats.setRule(ruleRepository.findById(ruleId).orElseThrow());
                    newStats.setCount(0);
                    return newStats;
                });
        stats.setCount(stats.getCount() + 1);
        ruleStatsRepository.save(stats);
    }

    public List<RuleStatsDto> getAllStats() {
        return ruleStatsRepository.findAllRulesWithStats().stream()
                .map(obj -> new RuleStatsDto((Long) obj[0], ((Number) obj[1]).intValue()))
                .collect(Collectors.toList());
    }
}