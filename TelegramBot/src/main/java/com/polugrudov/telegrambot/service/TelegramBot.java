package com.polugrudov.telegrambot.service;

import com.polugrudov.telegrambot.config.BotConfig;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    public TelegramBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public String getBotToken() {
        return botConfig.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageTest = update.getMessage().getText();

            long chatId = update.getMessage().getChatId();

            switch (messageTest) {
                case "/start":
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                default:
                    startCommandReceived(chatId, "Sorry, this command not supported now...");
            }
        }

    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Hi " + name;

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {

        }
    }

}
