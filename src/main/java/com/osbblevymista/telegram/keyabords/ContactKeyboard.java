package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class ContactKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonHeadOfOsbb = new OSBBKeyboardButton(Actions.BUTTON_HEAD_OF_OSBB.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAccount = new OSBBKeyboardButton(Actions.BUTTON_ACCOUNTANT.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonWorker = new OSBBKeyboardButton(Actions.BUTTON_WORKER.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonWorkSecurity = new OSBBKeyboardButton(Actions.BUTTON_SECURITY.getText());

    {
        osbbKeyboardButtonHeadOfOsbb.setId(Actions.BUTTON_HEAD_OF_OSBB.getText());
        osbbKeyboardButtonHeadOfOsbb.messages.add(Messages.CONTACT_HEAD_OF_OSBB.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonHeadOfOsbb);

        osbbKeyboardButtonAccount.setId(Actions.BUTTON_ACCOUNTANT.getText());
        osbbKeyboardButtonAccount.messages.add(Messages.CONTACT_ACCOUNTANT.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonAccount);

        osbbKeyboardButtonWorker.setId(Actions.BUTTON_WORKER.getText());
        osbbKeyboardButtonWorker.messages.add(Messages.CONTACT_BUTTON_WORKER.getMessage());
        insertIntoSecondRow(osbbKeyboardButtonWorker);

        osbbKeyboardButtonWorkSecurity.setId(Actions.BUTTON_SECURITY.getText());
        osbbKeyboardButtonWorkSecurity.messages.add(Messages.CONTACT_BUTTON_SECURITY.getMessage());
        insertIntoSecondRow(osbbKeyboardButtonWorkSecurity);
    }

    public ContactKeyboard(boolean isAdmin) {
        super(isAdmin, "\u200B"); // need an invisible character to identify which back button
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
