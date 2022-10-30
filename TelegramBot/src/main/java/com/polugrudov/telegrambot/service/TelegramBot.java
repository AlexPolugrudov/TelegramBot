package com.polugrudov.telegrambot.service;

import com.polugrudov.telegrambot.config.BotConfig;
import com.polugrudov.telegrambot.entity.SearchSubject;
import com.polugrudov.telegrambot.parser.ParserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig botConfig;

    private final ParserService parserService;

    public static final String START = "/start";
    public static final String FIND = "/find";

    public TelegramBot(BotConfig botConfig, ParserService parserService) {
        this.botConfig = botConfig;
        this.parserService = parserService;
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
                case START:
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
                case FIND:
                    sendFindMessage(chatId);
                    break;
                default:
                    findSearchSubject(chatId, messageTest);
            }
        }

    }

    private void startCommandReceived(long chatId, String name) {

        String answer = "Привет, " + name + "\n" + "Я бот, который ищет рейтинг товара, для поиска просто введи необходимое название, и я все сделаю";

        log.info("Replied to user " + name);

        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    private void sendFindMessage(long chatId) {
        SendMessage message = new SendMessage();

        message.setChatId(String.valueOf(chatId));

        message.setText("Введите название искомого предмета:");

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Ошибка при отправке: " + e.getMessage());
        }
    }

    private void findSearchSubject(long chatId, String searchItemName) {
//        SearchSubject searchSubjectOtzovik = parserService.findSubject(searchItemName).get(0);
//        SearchSubject searchSubjectIRecommend = parserService.findSubject(searchItemName).get(1);

        List<SearchSubject> subjectList = parserService.findSubject(searchItemName);

        SendMessage message = new SendMessage();

        editMessage(chatId, message, subjectList);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException("Ошибка при отправке сообщения");
        }
    }

    private void editMessage(long chatId, SendMessage message, List<SearchSubject> subjectList) {
        message.setChatId(String.valueOf(chatId));

        SearchSubject otzovikSubject = subjectList.get(0);
        SearchSubject iRecommnedSubject = subjectList.get(1);

        message.setText(
                "Отзыв с Otzovik:\n" +
                        "Название: " + otzovikSubject.getName() + "\n" +
                        "Средний рейтинг: " + otzovikSubject.getAverageRate() + "\n" +
                        "Ссылка на otzovik: " + otzovikSubject.getUrl() + "\n" +
                        "Оценки: \n" + otzovikSubject.getRates() + "\n\n" +
                        "Отзыв с Irecommend:\n" +
                        "Название: " + iRecommnedSubject.getName() + "\n" +
                        "Средний рейтинг: " + iRecommnedSubject.getAverageRate() + "\n" +
                        "Ссылка на IRecommend: " + iRecommnedSubject.getUrl() + "\n" +
                        "Оценки: \n" + iRecommnedSubject.getRates()
        );
    }
}
