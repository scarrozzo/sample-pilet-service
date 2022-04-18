package io.piral.feedservice.command;

public interface Command<T> {
    T execute();
}
