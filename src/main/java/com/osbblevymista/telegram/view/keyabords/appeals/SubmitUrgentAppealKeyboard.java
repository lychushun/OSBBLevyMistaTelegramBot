package com.osbblevymista.telegram.view.keyabords.appeals;

import com.osbblevymista.telegram.view.keyabords.OSBBKeyboard;
import com.osbblevymista.telegram.view.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.InvisibleCharacters;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class SubmitUrgentAppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonApprove = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_URGENT_APPROVE.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonCancel = new OSBBKeyboardButton(Actions.BUTTON_SEND_MESSAGE_CANCEL.getText());

    {
        osbbKeyboardButtonApprove.setId(Actions.BUTTON_APPEAL_URGENT_APPROVE.getText());
        insertIntoFirstRow(osbbKeyboardButtonApprove);

        osbbKeyboardButtonCancel.setId(Actions.BUTTON_SEND_MESSAGE_CANCEL.getText());
        insertIntoSecondRow(osbbKeyboardButtonCancel);
    }

    public SubmitUrgentAppealKeyboard(boolean isAdmin) {
        super(isAdmin, InvisibleCharacters._200C.getVal());
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }
}
