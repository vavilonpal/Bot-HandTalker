package org.global.handtalk.service;


import org.telegram.telegrambots.meta.api.methods.GetFile;

import org.telegram.telegrambots.meta.api.objects.File;
import org.global.handtalk.exception.VideoNoteNotExistException;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.VideoNote;

@Service
public class VideoNoteService {

    public void handleVideoNote(VideoNote videoNote) {
        if (videoNote != null) {
            throw new VideoNoteNotExistException("Video Note Doesn't exist");
        }

        try {
            String fileId = videoNote.getFileId();

            GetFile getFile = new GetFile(fileId);
            File file = execute(getFile);

        }



    }
}
