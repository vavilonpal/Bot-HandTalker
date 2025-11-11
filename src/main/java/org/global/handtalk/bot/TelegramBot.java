package org.global.handtalk.bot;

import lombok.RequiredArgsConstructor;
import org.global.handtalk.service.MessageService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
@RequiredArgsConstructor
public class TelegramBot  extends TelegramLongPollingBot {
    private final BotProperties botProperties;
    private final MessageService messageService;


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String text = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            switch (text) {
                //Help commands
                case "/start" -> messageService.sendStartMessage(this, chatId, update.getMessage().getChat().getUserName());
                case "/help" -> messageService.sendTextMessage(this, chatId, "ℹ️ Help info...");
                // Video commands
                case "/sign-to-speech" -> messageService.sendTextMessage(this, chatId, "Send Video Note!");
                case "/sign-to-text" -> messageService.sendTextMessage(this, chatId, " Send Video Note!");

                case "/about" -> messageService.sendTextMessage(this, chatId, "🤖 Hand Talker Bot!");
                case "/settings" -> messageService.sendTextMessage(this, chatId, "🤖 Settings ...");

                default -> messageService.sendTextMessage(this, chatId, "❓ Unknown command");
            }
        }



           /* // ------ VideoNote processing ------------
            if (incomeMessage.hasVideoNote()){
                VideoNote videoNote = update.getMessage().getVideoNote();

            }
        }*/
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
