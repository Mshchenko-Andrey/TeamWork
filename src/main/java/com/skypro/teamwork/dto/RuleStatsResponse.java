package com.skypro.teamwork.dto;

import java.util.List;

public class RuleStatsResponse {
    private List<RuleStatsDto> stats;

    public RuleStatsResponse(List<RuleStatsDto> stats) {
        this.stats = stats;
    }

    public List<RuleStatsDto> getStats() { return stats; }
    public void setStats(List<RuleStatsDto> stats) { this.stats = stats; }
}