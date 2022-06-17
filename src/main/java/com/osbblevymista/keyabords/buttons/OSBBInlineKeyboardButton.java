package com.osbblevymista.keyabords.buttons;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.HashMap;

@Getter
@Setter
public class OSBBInlineKeyboardButton extends InlineKeyboardButton {

    private String id;

    public HashMap<String, String> messages = new HashMap<>();

    public OSBBInlineKeyboardButton(){}

    public OSBBInlineKeyboardButton(String text){
        super(text);
    }

    public void doExecute(){

    }

    public boolean canExecute(){
        return false;
    }

    public OSBBInlineKeyboardButton(@NonNull String text, String url) {
        super(text, url, null, null, null, null, null, null, null);
    }

    public OSBBInlineKeyboardButton(@NonNull String text, String url, String callbackData) {
        super(text, url, callbackData, null, null, null, null, null, null);
    }
}
