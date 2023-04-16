package com.osbblevymista.telegram.dto.message;

public abstract class TelegramMessage<T> {

    public abstract T getContent();
    public abstract String getType();

}
