package io.piral.feedservice.exception;

public class DownloadFileException extends RuntimeException {

    public DownloadFileException(String message) {
        super(message);
    }

    public DownloadFileException(String message, Throwable cause) {
        super(message, cause);
    }
}