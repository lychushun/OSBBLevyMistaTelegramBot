package com.osbblevymista.telegram.dto.message;

import lombok.RequiredArgsConstructor;
import org.telegram.telegrambots.meta.api.objects.Document;

@RequiredArgsConstructor
public class DocumentTelegramMessage extends TelegramMessage<Document> {

    public static final String TYPE = "DocumentTelegramMessage";

    private final Document file;

    @Override
    public Document getContent() {
        return file;
    }

    @Override
    public String getType() {
        return TYPE;
    }
}
