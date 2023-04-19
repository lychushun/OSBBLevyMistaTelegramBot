package com.osbblevymista.telegram.view.pages;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.view.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Links;
import com.osbblevymista.telegram.system.Messages;
import com.osbblevymista.telegram.system.Titles;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.function.Function;

public class ReportPage extends BasePage{

    private static ReportPage reportPage = null;

    public static ReportPage getInstance(boolean isAdmin){
        if (reportPage == null){
            reportPage = new ReportPage();
        }
        reportPage.setAdmin(isAdmin);

        return reportPage;
    }

    private ReportPage(){
        super();
        title = Titles.REPORTS.getTitle();
    }

    @Override
    public void doExecute() {
    }

    @Override
    public boolean doExecute(PageParams sendMessageParams, Function<ExecutorListenerResponse, Boolean> consumer) throws UnsupportedEncodingException, URISyntaxException {

        ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
        executorListenerResponse.messages.add(Messages.REPORTS.getMessage());

        OSBBInlineKeyboardButton osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_RULES_OF_LEAVING.getText(), Links.RULES_OF_LEAVING.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_RULES_OF_LEAVING.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_TARIFFS2023.getText(), Links.TARIFFS2023.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_TARIFFS2023.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_PRESENTATION2023.getText(), Links.PRESENTATION2023.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_PRESENTATION2023.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_DEBTORS.getText(), Links.DEBTORS.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_DEBTORS.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        return consumer.apply(executorListenerResponse);
    }

    @Override
    public boolean canExecute() {
        return true;
    }

}
