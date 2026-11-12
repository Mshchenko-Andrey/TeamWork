package com.skypro.teamwork.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "rule_stats")
public class RuleStatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rule_id", nullable = false)
    private RuleEntity rule;

    @Column(name = "count", nullable = false)
    private Integer count = 0;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public RuleEntity getRule() { return rule; }
    public void setRule(RuleEntity rule) { this.rule = rule; }

    public Integer getCount() { return count; }
    public void setCount(Integer count) { this.count = count; }
}