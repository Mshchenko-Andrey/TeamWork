package com.skypro.teamwork.service;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleQueryDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DynamicRuleService {

    private final RuleService ruleService;
    private final RecommendationsRepository recommendationsRepository;
    private final StatsService statsService;

    public DynamicRuleService(RuleService ruleService,
                              RecommendationsRepository recommendationsRepository,
                              StatsService statsService) {
        this.ruleService = ruleService;
        this.recommendationsRepository = recommendationsRepository;
        this.statsService = statsService;
    }

    public Optional<RecommendationDto> checkDynamicRules(UUID userId) {
        List<RuleDto> allRules = ruleService.getAllRules();

        for (RuleDto rule : allRules) {
            boolean allQueriesMatch = true;

            for (RuleQueryDto query : rule.getRule()) {
                boolean result = executeQuery(userId, query);
                if (query.isNegate()) {
                    result = !result;
                }
                if (!result) {
                    allQueriesMatch = false;
                    break;
                }
            }

            if (allQueriesMatch) {
                statsService.incrementRuleCounter(rule.getId());
                return Optional.of(new RecommendationDto(
                        rule.getProductId(),
                        rule.getProductName(),
                        rule.getProductText()
                ));
            }
        }
        return Optional.empty();
    }

    private boolean executeQuery(UUID userId, RuleQueryDto query) {
        String type = query.getQuery();
        List<String> args = query.getArguments();

        return switch (type) {
            case "USER_OF" -> recommendationsRepository.checkUserOf(userId, args.get(0));
            case "ACTIVE_USER_OF" -> recommendationsRepository.checkActiveUserOf(userId, args.get(0));
            case "TRANSACTION_SUM_COMPARE" -> recommendationsRepository.checkTransactionSumCompare(
                    userId, args.get(0), args.get(1), args.get(2), Integer.parseInt(args.get(3))
            );
            case "TRANSACTION_SUM_COMPARE_DEPOSIT_WITHDRAW" -> recommendationsRepository.checkTransactionSumCompareDepositWithdraw(
                    userId, args.get(0), args.get(1)
            );
            default -> throw new IllegalArgumentException("Неизвестный тип запроса: " + type);
        };
    }
}