package com.osbblevymista.telegram.view.pages;

import com.osbblevymista.telegram.system.Titles;

public class SendMessagePage extends BasePage {

    private static SendMessagePage sendPage = null;

    public static SendMessagePage getInstance(boolean isAdmin){
        if (sendPage == null){
            sendPage = new SendMessagePage();
        }
        sendPage.setAdmin(isAdmin);

        return sendPage;
    }

    private SendMessagePage(){
        super();
        title = Titles.SEND_MESSAGE.getTitle();
    }

}
