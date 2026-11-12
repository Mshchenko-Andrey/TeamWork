package com.skypro.teamwork.bot;

import com.skypro.teamwork.dto.RecommendationDto;
import com.skypro.teamwork.repository.RecommendationsRepository;
import com.skypro.teamwork.service.RecommendationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public class RecommendationTelegramBot extends TelegramLongPollingBot {

    private final RecommendationService recommendationService;
    private final RecommendationsRepository recommendationsRepository;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Value("${telegram.bot.token}")
    private String botToken;

    public RecommendationTelegramBot(RecommendationService recommendationService,
                                     RecommendationsRepository recommendationsRepository) {
        this.recommendationService = recommendationService;
        this.recommendationsRepository = recommendationsRepository;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            if (messageText.startsWith("/recommend")) {
                String[] parts = messageText.split(" ", 3);
                if (parts.length < 3) {
                    sendMessage(chatId, "Пожалуйста, укажите имя и фамилию: /recommend Иван Иванов");
                    return;
                }
                String firstName = parts[1].trim();
                String lastName = parts[2].trim();

                List<Map<String, Object>> users = recommendationsRepository.findUsersByName(firstName, lastName);

                if (users.isEmpty() || users.size() > 1) {
                    sendMessage(chatId, "Пользователь не найден");
                    return;
                }


                Map<String, Object> user = users.get(0);
                UUID userId = (UUID) user.get("id");
                String fullName = user.get("first_name") + " " + user.get("last_name");


                List<RecommendationDto> recommendations = recommendationService.getRecommendations(userId);

                StringBuilder response = new StringBuilder();
                response.append("Здравствуйте ").append(fullName).append("\n");
                response.append("Новые продукты для вас:\n");

                if (recommendations.isEmpty()) {
                    response.append("- (Пока нет новых рекомендаций)");
                } else {
                    for (RecommendationDto rec : recommendations) {
                        response.append("- ").append(rec.getName()).append("\n");
                    }
                }

                sendMessage(chatId, response.toString());

            } else {
                sendMessage(chatId, "Привет! Я бот для рекомендаций. Напиши /recommend <имя> <фамилия>, чтобы получить рекомендации.");
            }
        }
    }

    private void sendMessage(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}