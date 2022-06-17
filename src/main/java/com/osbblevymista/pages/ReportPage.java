package com.osbblevymista.pages;

import com.osbblevymista.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Links;
import com.osbblevymista.system.Messages;

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
        title = "Звіти";
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
                new OSBBInlineKeyboardButton(Actions.BUTTON_TARIFFS.getText(), Links.TARIFFS.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_TARIFFS.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_MEETING_RELUSLT01022022.getText(), Links.MEETING_RESULT01022022.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_MEETING_RELUSLT01022022.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        return consumer.apply(executorListenerResponse);
    }

    @Override
    public boolean canExecute() {
        return true;
    }

}
