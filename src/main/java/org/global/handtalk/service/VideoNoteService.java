package org.global.handtalk.service;


import org.telegram.telegrambots.meta.api.methods.GetFile;

import org.telegram.telegrambots.meta.api.objects.File;
import org.global.handtalk.exception.VideoNoteNotExistException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.VideoNote;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

@Service
public class VideoNoteService {
    private final String outputDir = "videos/";


    public boolean saveVideo(AbsSender sender, VideoNote videoNote, String token) {
        String fileId = videoNote.getFileId();
        try {
            // Step 1: Get file info (to get file path)
            GetFile getFile = new GetFile(fileId);
            File file = sender.execute(getFile);

            // Step 2: Download the file from Telegram
            String fileUrl = "https://api.telegram.org/file/bot" + token + "/" + file.getFilePath();

            // Save to local storage
            String outputFile = outputDir + "video_note_" + fileId + ".mp4";
            try (InputStream in = new BufferedInputStream(new URL(fileUrl).openStream());
                 FileOutputStream out = new FileOutputStream(outputFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(buffer)) != -1) {
                    out.write(buffer, 0, bytesRead);
                }
            }
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    private void sendVideoToSignToSpeechService() {
    }


}
