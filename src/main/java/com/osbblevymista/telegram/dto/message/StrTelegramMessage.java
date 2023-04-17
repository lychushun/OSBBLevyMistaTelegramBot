package com.osbblevymista.telegram.dto.message;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StrTelegramMessage extends TelegramMessage<String> {

    public static final String TYPE = "StrTelegramMessage";
    public static final String SHORT_TYPE = "TEXT";

    private final String message;

    @Override
    public String getContent() {
        return message;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    @Override
    public String getTypeShort() {
        return SHORT_TYPE;
    }
}
