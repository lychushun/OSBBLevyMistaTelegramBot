package com.osbblevymista.telegram.dto.message;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Video;

@RequiredArgsConstructor
public class VideoTelegramMessage extends TelegramMessage<Video> {

    public static final String TYPE = "VideoTelegramMessage";
    public static final String SHORT_TYPE = "VIDEO";

    private final Video file;

    @Override
    public Video getContent() {
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
