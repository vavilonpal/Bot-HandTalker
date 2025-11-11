package org.global.handtalk.bot;

import lombok.RequiredArgsConstructor;
import org.global.handtalk.service.MessageService;
import org.global.handtalk.service.VideoNoteService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.File;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;

@Component
@RequiredArgsConstructor
public class TelegramBot extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final MessageService messageService;
    private final VideoNoteService videoNoteService;


    @Override
    public void onUpdateReceived(Update update) {
        // ------ Text processing ------------
        textProcess(update);

        // ------ VideoNote processing ------------
        videoNoteProcess(update);

    }


    private void videoNoteProcess(Update update) {
        Message incomeMessage = update.getMessage();
        Long chatId = incomeMessage.getChatId();

        if (update.hasMessage() && update.getMessage().hasVideoNote()) {
            VideoNote videoNote = update.getMessage().getVideoNote();

            videoNoteService.saveVideo(this, videoNote, getBotToken());

            SendMessage msg = new SendMessage(chatId.toString(),
                    "✅ I received your video note and saved");


            try {
                execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }


    }

    private void textProcess(Update update) {
        Message incomeMessage = update.getMessage();
        Long chatId = incomeMessage.getChatId();

        if (update.hasMessage() && incomeMessage.hasText()) {
            String text = incomeMessage.getText();
            switch (text) {
                //Help commands
                case "/start" -> messageService.sendStartMessage(this, chatId, incomeMessage.getChat().getUserName());
                case "/help" -> messageService.sendTextMessage(this, chatId, "ℹ️ Help info...");

                // Video commands
                case "/sign-to-speech" -> messageService.sendTextMessage(this, chatId, "Send Video Note!");
                case "/sign-to-text" -> messageService.sendTextMessage(this, chatId, " Send Video Note!");


                case "/about" -> messageService.sendTextMessage(this, chatId, "🤖 Hand Talker Bot!");
                case "/settings" -> messageService.sendTextMessage(this, chatId, "🤖 Settings ...");

                default -> messageService.sendTextMessage(this, chatId, "❓ Unknown command");
            }
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
