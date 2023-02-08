package com.osbblevymista.telegram.keyabords.appeals;

import com.osbblevymista.telegram.keyabords.OSBBKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class SubmitUrgentAppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonApprove = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_URGENT_APPROVE.getText());

    {
        osbbKeyboardButtonApprove.setId(Actions.BUTTON_APPEAL_URGENT_APPROVE.getText());
        insertIntoFirstRow(osbbKeyboardButtonApprove);
    }

    public SubmitUrgentAppealKeyboard(boolean isAdmin) {
        super(isAdmin, "\u200C");
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }
}
