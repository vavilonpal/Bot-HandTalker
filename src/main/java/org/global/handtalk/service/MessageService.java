package org.global.handtalk.service;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
public class MessageService {

    public void startMessage(Long chatId) {
        String commands = """
                👋 Welcome to my bot! Here are available commands:

                /help - Show help
                /info - Get your profile info
                /settings - Change your preferences
                /about - Learn about this bot
                """;

        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(commands);

    }
}
