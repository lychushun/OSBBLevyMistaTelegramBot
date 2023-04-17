package com.osbblevymista.telegram.dto.message;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;

@RequiredArgsConstructor
public class PhotoTelegramMessage extends TelegramMessage<PhotoSize> {

    public static final String TYPE = "PhotoTelegramMessage";
    public static final String SHORT_TYPE = "PHOTO";

    private final PhotoSize file;

    @Override
    public PhotoSize getContent() {
        return file;
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
