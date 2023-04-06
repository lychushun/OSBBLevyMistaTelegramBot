package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.InvisibleCharacters;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class ContactKeyboard1 extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonHeadOfOsbb = new OSBBKeyboardButton(Actions.BUTTON_HEAD_OF_OSBB.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAccount = new OSBBKeyboardButton(Actions.BUTTON_ACCOUNTANT.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonWorker = new OSBBKeyboardButton(Actions.BUTTON_WORKER.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonWorkSecurity = new OSBBKeyboardButton(Actions.BUTTON_SECURITY.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonBarrier = new OSBBKeyboardButton(Actions.BUTTON_BARRIER_PAGE.getText());

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

        osbbKeyboardButtonBarrier.setId(Actions.BUTTON_BARRIER_PAGE.getText());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER1.getMessage());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER5.getMessage());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER2.getMessage());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER3.getMessage());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER4.getMessage());
        osbbKeyboardButtonBarrier.messages.add(Messages.BUTTON_BARRIER6.getMessage());

        insertIntoThirdRow(osbbKeyboardButtonBarrier);
    }

    public ContactKeyboard1(boolean isAdmin) {
        super(isAdmin, InvisibleCharacters._200B.getVal()); // need an invisible character to identify which back button
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
