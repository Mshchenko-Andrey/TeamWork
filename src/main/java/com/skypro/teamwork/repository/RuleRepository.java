package com.skypro.teamwork.repository;

import com.skypro.teamwork.entity.RuleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleRepository extends JpaRepository<RuleEntity, Long> {
    void deleteByProductId(UUID productId);
}