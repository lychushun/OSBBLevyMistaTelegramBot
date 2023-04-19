package com.osbblevymista.telegram.dto.telegrammessage;

public abstract class TelegramMessage<T> {

    public abstract T getContent();
    public abstract String getType();
    public abstract String getTypeShort();

}
