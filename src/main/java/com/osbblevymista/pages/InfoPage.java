package com.osbblevymista.pages;

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
        title = "Інформація";
    }

}
