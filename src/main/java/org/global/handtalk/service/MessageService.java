package org.global.handtalk.service;

import org.springframework.stereotype.Service;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Service
public class MessageService {


    public void sendTextMessage(AbsSender sender, Long chatId, String text) {
        SendMessage message = new SendMessage(chatId.toString(), text);
        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public void sendStartMessage(AbsSender sender, Long chatId, String username) {
        ReplyKeyboardMarkup keyboard = commandKeyboard();

        String welcomeText = "Hi, " + username + ", nice to meet you!" + "\n" +
                "This is what I can do:" + "\n" +
                "1. Translate Sign Language into text." + "\n" +
                "2. Voice the text and overlay it on the video" + "\n" +
                "Select the command you need:" + "\n";


        SendMessage message = new SendMessage();
        message.setChatId(chatId.toString());
        message.setText(welcomeText);
        message.setReplyMarkup(keyboard);

        try {
            sender.execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup commandKeyboard() {
        ReplyKeyboardMarkup keyboard = new ReplyKeyboardMarkup();
        keyboard.setResizeKeyboard(true); // fit to screen
        keyboard.setOneTimeKeyboard(false); // stays visible


        List<KeyboardRow> rows = new ArrayList<>();

        KeyboardRow firstMainCommandsRow = new KeyboardRow();
        firstMainCommandsRow.add(new KeyboardButton("/sign-to-speech"));
        firstMainCommandsRow.add(new KeyboardButton("/sign-to-text"));

        KeyboardRow secondHelpRow = new KeyboardRow();
        secondHelpRow.add(new KeyboardButton("/help"));
        secondHelpRow.add(new KeyboardButton("/info"));

        KeyboardRow thirdSettingsAndAboutRow = new KeyboardRow();
        thirdSettingsAndAboutRow.add(new KeyboardButton("/settings"));
        thirdSettingsAndAboutRow.add(new KeyboardButton("/about"));

        rows.add(firstMainCommandsRow);
        rows.add(secondHelpRow);
        rows.add(thirdSettingsAndAboutRow);

        keyboard.setKeyboard(rows);

        return keyboard;
    }
}
