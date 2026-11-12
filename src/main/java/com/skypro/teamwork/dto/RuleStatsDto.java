package com.skypro.teamwork.dto;

public class RuleStatsDto {
    private Long ruleId;
    private Integer count;

    public RuleStatsDto(Long ruleId, Integer count) {
        this.ruleId = ruleId;
        this.count = count;
    }

    public Long getRuleId() { return ruleId; }
    public void setRuleId(Long ruleId) { this.ruleId = ruleId; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}