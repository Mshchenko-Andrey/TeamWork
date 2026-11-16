package com.skypro.teamwork.repository;

import com.skypro.teamwork.entity.RuleStatsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RuleStatsRepository extends JpaRepository<RuleStatsEntity, Long> {
    Optional<RuleStatsEntity> findByRuleId(Long ruleId);

    @Query("SELECT r.id, COALESCE(s.count, 0) FROM RuleEntity r LEFT JOIN RuleStatsEntity s ON r.id = s.rule.id")
    List<Object[]> findAllRulesWithStats();
}