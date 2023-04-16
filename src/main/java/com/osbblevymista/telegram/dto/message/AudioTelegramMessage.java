package com.osbblevymista.telegram.dto.message;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Audio;

@RequiredArgsConstructor
public class AudioTelegramMessage extends TelegramMessage<Audio> {

    public static final String TYPE = "AudioTelegramMessage";

    private final Audio file;

    @Override
    public Audio getContent() {
        return file;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
