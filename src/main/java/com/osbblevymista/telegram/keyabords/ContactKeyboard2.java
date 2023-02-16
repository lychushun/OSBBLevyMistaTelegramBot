package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.InvisibleCharacters;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class ContactKeyboard2 extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonCityLift = new OSBBKeyboardButton(Actions.BUTTON_CITYLIFT.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonRepaipofboilers = new OSBBKeyboardButton(Actions.BUTTON_REPAIROFBOILERS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonDomofon = new OSBBKeyboardButton(Actions.BUTTON_DOMOFONSERVICE.getText());


    {
        osbbKeyboardButtonCityLift.setId(Actions.BUTTON_CITYLIFT.getText());
        osbbKeyboardButtonCityLift.messages.add(Messages.CONTACT_BUTTON_CITYLIFT.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonCityLift);

        osbbKeyboardButtonRepaipofboilers.setId(Actions.BUTTON_REPAIROFBOILERS.getText());
        osbbKeyboardButtonRepaipofboilers.messages.add(Messages.CONTACT_BUTTON_REPAIROFBOILERS.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonRepaipofboilers);

        osbbKeyboardButtonDomofon.setId(Actions.BUTTON_DOMOFONSERVICE.getText());
        osbbKeyboardButtonDomofon.messages.add(Messages.CONTACT_BUTTON_DOMOFONSERVICE.getMessage());
        insertIntoSecondRow(osbbKeyboardButtonDomofon);
    }

    public ContactKeyboard2(boolean isAdmin) {
        super(isAdmin, InvisibleCharacters._3000.getVal()); // need an invisible character to identify which back button
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
