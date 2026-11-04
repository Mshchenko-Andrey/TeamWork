package com.skypro.teamwork;

import com.skypro.teamwork.repository.RecommendationsRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

@SpringBootTest
public class RecommendationsRepositoryTest {

    @Autowired
    private RecommendationsRepository repository;

    @Test
    void testNewQueries() {
        UUID userId = UUID.fromString("cd515076-5d8a-44be-930e-8d4fcb79f42d");


        boolean hasDebit = repository.hasProductType(userId, "DEBIT");
        System.out.println("Есть ли DEBIT: " + hasDebit);


        boolean hasInvest = repository.hasProductType(userId, "INVEST");
        System.out.println("Есть ли INVEST: " + hasInvest);


        int sumSavingDeposit = repository.getSumByProductTypeAndTransactionType(userId, "SAVING", "DEPOSIT");
        System.out.println("Сумма пополнений SAVING: " + sumSavingDeposit);


        int sumDebitDeposit = repository.getSumByProductTypeAndTransactionType(userId, "DEBIT", "DEPOSIT");
        System.out.println("Сумма пополнений DEBIT: " + sumDebitDeposit);
    }
}