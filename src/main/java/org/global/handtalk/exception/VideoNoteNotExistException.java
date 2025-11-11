package org.global.handtalk.exception;


public class VideoNoteNotExistException extends RuntimeException{
    public VideoNoteNotExistException() {
        super();
    }

    public VideoNoteNotExistException(String message) {
        super(message);
    }

    public VideoNoteNotExistException(String message, Throwable cause) {
        super(message, cause);
    }
}
