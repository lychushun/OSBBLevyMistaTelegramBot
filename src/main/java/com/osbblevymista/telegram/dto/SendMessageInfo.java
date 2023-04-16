package com.osbblevymista.telegram.dto;

import lombok.Builder;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Comparator.comparing;
import static org.thymeleaf.util.StringUtils.isEmpty;

@Builder
@Getter
public class SendMessageInfo {

    private PhotoSize photoSize;
    private Document document;
    private String command = "";
    private Message message;
    private Update update;

    public boolean isNotEmpty() {
        return photoSize != null || !isEmpty(command) || document != null;
    }

    public boolean hasPhoto(){
        return photoSize != null;
    }

    public Long getChatId() {
        return message.getChatId();
    }

    @Override
    public String toString() {
        return "REQUEST FROM USER ID: " + message.getFrom().getId() +
                "; FIRST NAME: " + message.getFrom().getFirstName() +
                "; LAST NAME: " + message.getFrom().getLastName() +
                "; USER NAME: " + message.getFrom().getUserName() +
                "; CHAT ID: " + message.getChatId() +
                "; MESSAGE: " + message.getText();
    }

    public boolean hasDocument() {
        return document != null;
    }


    public static class SendMessageInfoBuilder {
        private PhotoSize photoSizeList;
        private Document document;
        private String command;
        private Message message;
        private Update update;

        public SendMessageInfo build() {

            if (update != null) {
                message = getMessage(update);
                command = getCommand(update);
            }

            if (message != null){
                if ( message.getPhoto() != null) {
                    photoSizeList = message.getPhoto()
                            .stream()
                            .max(comparing(PhotoSize::getFileSize)).get();
                }

                if (message.getDocument() != null){
                    document = message.getDocument();
                }
            }

            return new SendMessageInfo(photoSizeList, document, command, message, update);
        }

        public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) {
            Map<Object, Boolean> map = new ConcurrentHashMap<>();
            return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
        }

        private String getCommand(Update update) {
            Message message = update.getMessage();
            if (message != null && !isEmpty(message.getText())) {
                return message.getText();
            } else if (update.hasCallbackQuery()) {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                return callbackQuery.getData();
            } else {
                return null;
            }
        }

        private Message getMessage(Update update) {
            Message message = update.getMessage();
            if (message != null) {
                return message;
            } else {
                CallbackQuery callbackQuery = update.getCallbackQuery();
                if (callbackQuery != null) {
                    return update.getCallbackQuery().getMessage();
                }
            }
            return null;
        }


    }

}
