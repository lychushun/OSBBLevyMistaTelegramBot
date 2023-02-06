package com.osbblevymista.telegram.pages;

import com.osbblevymista.telegram.system.Titles;

public class InfoPage extends BasePage{

    private static InfoPage infoPage = null;

    public static InfoPage getInstance(boolean isAdmin){
        if (infoPage == null){
            infoPage = new InfoPage();
        }
        infoPage.setAdmin(isAdmin);

        return infoPage;
    }

    private InfoPage(){
        super();
        title = Titles.INFO.getTitle();
    }

}
