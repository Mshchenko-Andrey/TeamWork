package com.skypro.teamwork;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import com.skypro.teamwork.service.rule.Invest500RuleSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class Invest500RuleSetTest {

    @Mock
    private RecommendationsRepository repository;

    @InjectMocks
    private Invest500RuleSet ruleSet;

    @Test
    void shouldReturnRecommendationWhenRulesMatch() {
        UUID userId = UUID.randomUUID();


        when(repository.hasProductType(userId, "DEBIT")).thenReturn(true);
        when(repository.hasProductType(userId, "INVEST")).thenReturn(false);
        when(repository.getSumByProductTypeAndTransactionType(userId, "SAVING", "DEPOSIT")).thenReturn(1500);


        Optional<RecommendationDto> result = ruleSet.check(userId);


        assertTrue(result.isPresent());
        assertEquals("Invest 500", result.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenNoDebit() {
        UUID userId = UUID.randomUUID();


        when(repository.hasProductType(userId, "DEBIT")).thenReturn(false);

        Optional<RecommendationDto> result = ruleSet.check(userId);


        assertTrue(result.isEmpty());
    }
}