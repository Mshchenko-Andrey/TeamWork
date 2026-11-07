package com.skypro.teamwork.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.UUID;

@Repository
public class RecommendationsRepository {

    private final JdbcTemplate jdbcTemplate;

    // Настраиваем 3 кеша (по одному на каждый метод)
    private final Cache<String, Boolean> userOfCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(1000)
            .build();

    private final Cache<String, Boolean> activeUserOfCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(1000)
            .build();

    private final Cache<String, Integer> sumCache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(1000)
            .build();

    public RecommendationsRepository(@Qualifier("recommendationsJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // --- МЕТОДЫ ДЛЯ СТАРЫХ ФИКСИРОВАННЫХ ПРАВИЛ ---
    public boolean hasProductType(UUID userId, String productType) {
        return checkUserOf(userId, productType);
    }

    public int getSumByProductTypeAndTransactionType(UUID userId, String productType, String transactionType) {
        String key = userId + "_" + productType + "_" + transactionType;
        return sumCache.get(key, k -> {
            String sql = """
                    SELECT COALESCE(SUM(t.amount), 0)
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ? AND t.type = ?
                    """;
            Integer sum = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType, transactionType);
            return sum != null ? sum : 0;
        });
    }

    // --- НОВЫЕ МЕТОДЫ ДЛЯ ДИНАМИЧЕСКИХ ПРАВИЛ (С КЕШЕМ) ---

    public boolean checkUserOf(UUID userId, String productType) {
        String key = userId + "_" + productType;
        return userOfCache.get(key, k -> {
            String sql = """
                    SELECT COUNT(*)
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                    """;
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null && count > 0;
        });
    }

    public boolean checkActiveUserOf(UUID userId, String productType) {
        String key = userId + "_" + productType;
        return activeUserOfCache.get(key, k -> {
            String sql = """
                    SELECT COUNT(*)
                    FROM transactions t
                    JOIN products p ON t.product_id = p.id
                    WHERE t.user_id = ? AND p.type = ?
                    """;
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, userId, productType);
            return count != null && count >= 5;
        });
    }

    public boolean checkTransactionSumCompare(UUID userId, String productType, String transactionType, String operator, int constant) {
        int sum = getSumByProductTypeAndTransactionType(userId, productType, transactionType);
        return applyOperator(sum, constant, operator);
    }

    public boolean checkTransactionSumCompareDepositWithdraw(UUID userId, String productType, String operator) {
        int sumDeposit = getSumByProductTypeAndTransactionType(userId, productType, "DEPOSIT");
        int sumWithdraw = getSumByProductTypeAndTransactionType(userId, productType, "WITHDRAW");
        return applyOperator(sumDeposit, sumWithdraw, operator);
    }

    private boolean applyOperator(int left, int right, String operator) {
        return switch (operator) {
            case ">" -> left > right;
            case "<" -> left < right;
            case "=" -> left == right;
            case ">=" -> left >= right;
            case "<=" -> left <= right;
            default -> throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        };
    }
}