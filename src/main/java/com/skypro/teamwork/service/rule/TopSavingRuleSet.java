package com.skypro.teamwork.service.rule;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final RecommendationsRepository repository;

    public TopSavingRuleSet(RecommendationsRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<RecommendationDto> check(UUID userId) {
        boolean hasDebit = repository.hasProductType(userId, "DEBIT");
        if (!hasDebit) return Optional.empty();

        int sumDebitDeposit = repository.getSumByProductTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        int sumSavingDeposit = repository.getSumByProductTypeAndTransactionType(userId, "SAVING", "DEPOSIT");
        int sumDebitWithdrawal = repository.getSumByProductTypeAndTransactionType(userId, "DEBIT", "WITHDRAW");

        boolean condition1 = (sumDebitDeposit >= 50000) || (sumSavingDeposit >= 50000);
        boolean condition2 = sumDebitDeposit > sumDebitWithdrawal;

        if (condition1 && condition2) {
            return Optional.of(new RecommendationDto(
                    UUID.fromString("59efc529-2fff-41af-baff-90ccd7402925"),
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели. Больше никаких забытых чеков и потерянных квитанций — всё под контролем!\n\nПреимущества «Копилки»:\n\nНакопление средств на конкретные цели. Установите лимит и срок накопления, и банк будет автоматически переводить определенную сумму на ваш счет.\n\nПрозрачность и контроль. Отслеживайте свои доходы и расходы, контролируйте процесс накопления и корректируйте стратегию при необходимости.\n\nБезопасность и надежность. Ваши средства находятся под защитой банка, а доступ к ним возможен только через мобильное приложение или интернет-банкинг.\n\nНачните использовать «Копилку» уже сегодня и станьте ближе к своим финансовым целям!"
            ));
        }
        return Optional.empty();
    }
}