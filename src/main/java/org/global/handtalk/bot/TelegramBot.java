package org.global.handtalk.bot;

import lombok.RequiredArgsConstructor;
import org.global.handtalk.service.MessageService;
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


    @Override
    public void onUpdateReceived(Update update) {
        Message incomeMessage = update.getMessage();
        Long chatId = incomeMessage.getChatId();

        // ------ Text processing ------------
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

        // ------ VideoNote processing ------------
        if (update.hasMessage() && incomeMessage.hasVideoNote()) {
            VideoNote videoNote = incomeMessage.getVideoNote();
            String fileId = videoNote.getFileId();


            try {
                // Step 1: Get file info (to get file path)
                GetFile getFile = new GetFile(fileId);
                File file = execute(getFile);

                // Step 2: Download the file from Telegram
                String fileUrl = "https://api.telegram.org/file/bot" + getBotToken() + "/" + file.getFilePath();

                // Save to local storage
                String outputFile = "video_note_" + fileId + ".mp4";
                try (InputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
                     FileOutputStream out = new FileOutputStream(outputFile)) {

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = in.read(buffer)) != -1) {
                        out.write(buffer, 0, bytesRead);
                    }
                }

                // Notify user
                SendMessage msg = new SendMessage(chatId.toString(),
                        "✅ I received your video note and saved it as `" + outputFile + "`");
                execute(msg);

            } catch (TelegramApiException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
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
