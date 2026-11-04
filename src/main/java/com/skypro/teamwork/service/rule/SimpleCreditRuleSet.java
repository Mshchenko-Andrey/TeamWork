package com.skypro.teamwork.service.rule;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public SimpleCreditRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasCredit = repository.hasProductType(userId, "CREDIT");
        if (hasCredit) return Optional.empty();

        int sumDebitDeposit = repository.getSumByProductTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        int sumDebitWithdrawal = repository.getSumByProductTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");

        if (sumDebitDeposit > sumDebitWithdrawal && sumDebitWithdrawal > 100000) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("ab138afb-f3ba-4a93-b74f-0fcee86d447f"),
                    "Простой кредит",
                    "Откройте мир выгодных кредитов с нами!\n\nИщете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно! Мы предлагаем низкие процентные ставки, гибкие условия и индивидуальный подход к каждому клиенту.\n\nПочему выбирают нас:\n\nБыстрое рассмотрение заявки. Мы ценим ваше время, поэтому процесс рассмотрения заявки занимает всего несколько часов.\n\nУдобное оформление. Подать заявку на кредит можно онлайн на нашем сайте или в мобильном приложении.\n\nШирокий выбор кредитных продуктов. Мы предлагаем кредиты на различные цели: покупку недвижимости, автомобиля, образование, лечение и многое другое.\n\nНе упустите возможность воспользоваться выгодными условиями кредитования от нашей компании!"
            ));
        }
        return Optional.empty();
    }
}