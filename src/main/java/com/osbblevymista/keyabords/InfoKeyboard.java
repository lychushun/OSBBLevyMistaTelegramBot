package com.osbblevymista.keyabords;

import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.ContactPage;
import com.osbblevymista.pages.ReportPage;
import com.osbblevymista.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class InfoKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonContacts = new OSBBKeyboardButton(Actions.BUTTON_CONTACT.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonReports = new OSBBKeyboardButton(Actions.BUTTON_REPORTS.getText());


    {
        osbbKeyboardButtonContacts.setId(Actions.BUTTON_CONTACT.getText());
        insertIntoFirstRow(osbbKeyboardButtonContacts);

        osbbKeyboardButtonReports.setId(Actions.BUTTON_REPORTS.getText());
        insertIntoFirstRow(osbbKeyboardButtonReports);
    }

    public void setContactPage(ContactPage contactPage) {
        osbbKeyboardButtonContacts.setNextPage(contactPage);
    }

    public void setReportPage(ReportPage reportPage) {
        osbbKeyboardButtonReports.setNextPage(reportPage);
    }

    public InfoKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
