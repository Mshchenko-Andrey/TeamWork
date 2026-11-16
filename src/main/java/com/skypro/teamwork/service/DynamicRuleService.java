package com.skypro.teamwork.service;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.dto.RuleDto;
import com.skypro.teamwork.dto.RuleQueryDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Сервис для проверки динамических правил рекомендаций.
 * Загружает правила из базы данных PostgreSQL и проверяет их выполнение для конкретного пользователя.
 */
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

    /**
     * Проверяет все динамические правила для заданного пользователя.
     * Если правило срабатывает, возвращает рекомендацию и увеличивает счетчик статистики.
     *
     * @param userId ID пользователя (UUID).
     * @return Optional с рекомендацией, если найдено подходящее правило, иначе пустой Optional.
     */
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

    /**
     * Выполняет один запрос (условие) из динамического правила.
     */
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