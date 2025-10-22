package org.global.handtalk.bot;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot  extends TelegramLongPollingBot {
    private final BotProperties botProperties;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message incomeMessage = update.getMessage();
            Long chatId = incomeMessage.getChatId();

            if (incomeMessage.hasText()) {
                String text = incomeMessage.getText();

                if (text.equals("/start")) {
                    sendMessage(chatId, "I'm working");
                } else if (text.equals("/oladia")) {
                    sendMessage(chatId, "Привет пупсик ;)");
                } else {
                    sendMessage(chatId, "Unknown command!");
                }
            } else {
                sendMessage(chatId, "Incorrect data format!");
            }
        }
    }

    /*
     * On every message we must set chat id
     * */
    private void sendMessage(Long chatId, String messageToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(messageToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getBotUsername() {
        return this.botProperties.name;
    }

    @Override
    public String getBotToken() {
        return this.botProperties.token;
    }
}
