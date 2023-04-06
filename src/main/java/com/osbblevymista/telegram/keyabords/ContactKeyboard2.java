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

    private final OSBBKeyboardButton osbbKeyboardButtonLvivGaz = new OSBBKeyboardButton(Actions.BUTTON_LVIVGAZ.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonLvivoblenergo = new OSBBKeyboardButton(Actions.BUTTON_LVIVOBLENERGO.getText());

    {
        osbbKeyboardButtonCityLift.setId(Actions.BUTTON_CITYLIFT.getText());
        osbbKeyboardButtonCityLift.messages.add(Messages.CONTACT_BUTTON_CITYLIFT.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonCityLift);

        osbbKeyboardButtonDomofon.setId(Actions.BUTTON_DOMOFONSERVICE.getText());
        osbbKeyboardButtonDomofon.messages.add(Messages.CONTACT_BUTTON_DOMOFONSERVICE.getMessage());
        insertIntoFirstRow(osbbKeyboardButtonDomofon);

        osbbKeyboardButtonLvivoblenergo.setId(Actions.BUTTON_LVIVOBLENERGO.getText());
        osbbKeyboardButtonLvivoblenergo.messages.add(Messages.CONTACT_BUTTON_LVIVOBLENERGO.getMessage());
        insertIntoSecondRow(osbbKeyboardButtonLvivoblenergo);

        osbbKeyboardButtonLvivGaz.setId(Actions.BUTTON_LVIVGAZ.getText());
        osbbKeyboardButtonLvivGaz.messages.add(Messages.CONTACT_BUTTON_LVIVGAZ.getMessage());
        insertIntoSecondRow(osbbKeyboardButtonLvivGaz);

        osbbKeyboardButtonRepaipofboilers.setId(Actions.BUTTON_REPAIROFBOILERS.getText());
        osbbKeyboardButtonRepaipofboilers.messages.add(Messages.CONTACT_BUTTON_REPAIROFBOILERS.getMessage());
        insertIntoThirdRow(osbbKeyboardButtonRepaipofboilers);
    }

    public ContactKeyboard2(boolean isAdmin) {
        super(isAdmin, InvisibleCharacters._3000.getVal()); // need an invisible character to identify which back button
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
