package com.osbblevymista.executorlistener;

import com.osbblevymista.keyabords.buttons.OSBBInlineKeyboardButton;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.*;


@Getter
@Setter
public class ExecutorListenerResponse {

    private List<List<OSBBInlineKeyboardButton>> osbbInlineKeyboardButtonMap = new ArrayList<>();
    private List<List<InlineKeyboardButton>> inlineKeyboardButtons = new ArrayList<>();

    public List<String> messages = new ArrayList<>();

    private String title = "";

    public void insertOSBBInlineKeyboardButtonNextRow(OSBBInlineKeyboardButton osbbInlineKeyboardButton){
        if (Objects.isNull(osbbInlineKeyboardButton.getId())){
            throw new NullPointerException("OSBB Inline KeyboardButton id cannot be null");
        }
        List<OSBBInlineKeyboardButton> newRowOSBBInlineKeyboardButton = new ArrayList<OSBBInlineKeyboardButton>();
        newRowOSBBInlineKeyboardButton.add(osbbInlineKeyboardButton);

        osbbInlineKeyboardButtonMap.add(new ArrayList<OSBBInlineKeyboardButton>(newRowOSBBInlineKeyboardButton));

        InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
                .text(osbbInlineKeyboardButton.getText())
                .url(osbbInlineKeyboardButton.getUrl())
                .build();

        List<InlineKeyboardButton> newRowInlineKeyboardButton = new ArrayList<InlineKeyboardButton>();
        newRowInlineKeyboardButton.add(inlineKeyboardButton);
        inlineKeyboardButtons.add(newRowInlineKeyboardButton);
    }

    public void insertOSBBInlineKeyboardButtonNextCell(OSBBInlineKeyboardButton osbbInlineKeyboardButton){
        if (Objects.isNull(osbbInlineKeyboardButton.getId())){
            throw new NullPointerException("OSBB Inline KeyboardButton id cannot be null");
        }
        osbbInlineKeyboardButtonMap.get(osbbInlineKeyboardButtonMap.size() - 1).add(osbbInlineKeyboardButton);

        InlineKeyboardButton inlineKeyboardButton = InlineKeyboardButton.builder()
                .text(osbbInlineKeyboardButton.getText())
                .url(osbbInlineKeyboardButton.getUrl())
                .build();

        inlineKeyboardButtons.get(inlineKeyboardButtons.size() - 1).add(inlineKeyboardButton);
    }

}
