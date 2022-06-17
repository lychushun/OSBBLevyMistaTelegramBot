package com.osbblevymista.keyabords;

import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.ContactPage;
import com.osbblevymista.pages.ReportPage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class InfoKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonContacts = new OSBBKeyboardButton(MainKeyboard.BUTTONS.CONTACT.text);
    private final OSBBKeyboardButton osbbKeyboardButtonReports = new OSBBKeyboardButton(MainKeyboard.BUTTONS.REPORTS.text);


    {
        osbbKeyboardButtonContacts.setId(MainKeyboard.BUTTONS.CONTACT.text);
        insertIntoFirstRow(osbbKeyboardButtonContacts);

        osbbKeyboardButtonReports.setId(MainKeyboard.BUTTONS.REPORTS.text);
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
