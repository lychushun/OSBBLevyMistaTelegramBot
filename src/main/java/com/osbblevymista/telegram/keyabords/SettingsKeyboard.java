package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class SettingsKeyboard  extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonPass = new OSBBKeyboardButton(Actions.BUTTON_PASS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonLogin = new OSBBKeyboardButton(Actions.BUTTON_LOGIN.getText());

    {

        osbbKeyboardButtonLogin.setId(Actions.BUTTON_LOGIN.getText());
        osbbKeyboardButtonLogin.messages.add(Messages.INSERT_LOGIN.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonLogin);

        osbbKeyboardButtonPass.setId(Actions.BUTTON_PASS.getText());
        osbbKeyboardButtonPass.messages.add(Messages.INSERT_PASS.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonPass);

    }

    public SettingsKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
