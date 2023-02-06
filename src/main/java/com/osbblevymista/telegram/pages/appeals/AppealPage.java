package com.osbblevymista.telegram.pages.appeals;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.pages.BasePage;
import com.osbblevymista.telegram.pages.PageParams;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Links;
import com.osbblevymista.telegram.system.Messages;
import com.osbblevymista.telegram.system.Titles;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.function.Function;

public class AppealPage extends BasePage {
    private static AppealPage appealPage = null;

    public static AppealPage getInstance(boolean isAdmin){
        if (appealPage == null){
            appealPage = new AppealPage();
        }
        appealPage.setAdmin(isAdmin);

        return appealPage;
    }

    @Override
    public void doExecute() {
    }

    @Override
    public boolean doExecute(PageParams sendMessageParams, Function<ExecutorListenerResponse, Boolean> consumer) throws UnsupportedEncodingException, URISyntaxException {

        ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
        executorListenerResponse.messages.add(Messages.POLL.getMessage());

        OSBBInlineKeyboardButton osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_CAR_ENTRANCE.getText(), Links.CAR_ENTRANCE.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_CAR_ENTRANCE.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        return consumer.apply(executorListenerResponse);
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    private AppealPage(){
        super();
        title = Titles.APPEAL.getTitle();
    }

}
