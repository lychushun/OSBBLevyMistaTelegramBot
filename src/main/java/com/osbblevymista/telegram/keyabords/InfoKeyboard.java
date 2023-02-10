package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.ContactPage1;
import com.osbblevymista.telegram.pages.ReportPage;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Links;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;

public class InfoKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonContacts = new OSBBKeyboardButton(Actions.BUTTON_CONTACT.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonReports = new OSBBKeyboardButton(Actions.BUTTON_REPORTS.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonMiyDim = new OSBBKeyboardButton(Actions.BUTTON_USEFUL_LINKS.getText());
//    private final OSBBKeyboardButton osbbKeyboardButtonWebSite = new OSBBKeyboardButton(Actions.BUTTON_WEB.getText());


    {
        osbbKeyboardButtonContacts.setId(Actions.BUTTON_CONTACT.getText());
        insertIntoFirstRow(osbbKeyboardButtonContacts);

        osbbKeyboardButtonReports.setId(Actions.BUTTON_REPORTS.getText());
        insertIntoFirstRow(osbbKeyboardButtonReports);

        osbbKeyboardButtonMiyDim.setId(Actions.BUTTON_USEFUL_LINKS.getText());
        osbbKeyboardButtonMiyDim.setOnClickListener(new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.messages.add(Messages.USFUL_LINKS.getMessage());

                OSBBInlineKeyboardButton osbbInlineKeyboardButton =
                        new OSBBInlineKeyboardButton(Actions.BUTTON_MIYDIM_LINK.getText(), Links.MIYDIM.getLink());
                osbbInlineKeyboardButton.setId(Actions.BUTTON_MIYDIM_LINK.getText());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

                osbbInlineKeyboardButton =
                        new OSBBInlineKeyboardButton(Actions.BUTTON_WEB_LINK.getText(), Links.WEB.getLink());
                osbbInlineKeyboardButton.setId(Actions.BUTTON_WEB_LINK.getText());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

                return executorListenerResponse;
            }
        });
        insertIntoSecondRow(osbbKeyboardButtonMiyDim);

    }

    public void setContactPage(ContactPage1 contactPage) {
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
