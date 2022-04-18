package io.piral.feedservice.exception;

public class ParsePackageException extends RuntimeException {
    public ParsePackageException(String message) {
        super(message);
    }

    public ParsePackageException(String message, Throwable cause) {
        super(message, cause);
    }
}
