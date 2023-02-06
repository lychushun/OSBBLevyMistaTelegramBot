package com.osbblevymista.telegram.keyabords.appeals;

import com.osbblevymista.telegram.keyabords.OSBBKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class SubmitSimpleAppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonApprove = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_SIMPLE_APPROVE.getText());

    {
        insertIntoFirstRow(osbbKeyboardButtonApprove);
    }


    public SubmitSimpleAppealKeyboard(boolean isAdmin) {
        super(isAdmin, "\u200C");
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }
}
