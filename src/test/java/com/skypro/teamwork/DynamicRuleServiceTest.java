package com.skypro.teamwork;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleQueryDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import com.skypro.teamwork.service.DynamicRuleService;
import com.skypro.teamwork.service.RuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DynamicRuleServiceTest {

    @Mock
    private RuleService ruleService;

    @Mock
    private RecommendationsRepository recommendationsRepository;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    @Test
    void testCheckDynamicRules_WhenAllMatch() {
        UUID userId = UUID.randomUUID();


        RuleDto ruleDto = new RuleDto();
        ruleDto.setProductName("Простой кредит");
        ruleDto.setProductId(UUID.randomUUID());
        ruleDto.setProductText("Текст...");

        RuleQueryDto queryDto = new RuleQueryDto();
        queryDto.setQuery("USER_OF");
        queryDto.setArguments(List.of("DEBIT"));
        queryDto.setNegate(false);
        ruleDto.setRule(List.of(queryDto));


        when(ruleService.getAllRules()).thenReturn(List.of(ruleDto));
        when(recommendationsRepository.checkUserOf(userId, "DEBIT")).thenReturn(true);


        Optional<RecommendationDto> result = dynamicRuleService.checkDynamicRules(userId);


        assertTrue(result.isPresent());
        assertTrue(result.get().getName().equals("Простой кредит"));
    }
}