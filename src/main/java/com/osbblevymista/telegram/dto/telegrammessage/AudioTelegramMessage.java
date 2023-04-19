package com.osbblevymista.telegram.dto.telegrammessage;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Audio;

@RequiredArgsConstructor
public class AudioTelegramMessage extends TelegramMessage<Audio> {

    public static final String TYPE = "AudioTelegramMessage";
    public static final String SHORT_TYPE = "AUDIO";

    private final Audio file;

    @Override
    public Audio getContent() {
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
