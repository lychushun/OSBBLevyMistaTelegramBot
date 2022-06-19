package com.osbblevymista.pages;


import com.osbblevymista.system.Titles;

public class MainPage extends BasePage{

    private static MainPage mainPage = null;

    public static MainPage getInstance(boolean isAdmin){
        if (mainPage == null){
            mainPage = new MainPage(isAdmin);
        }
        mainPage.setAdmin(isAdmin);

        return mainPage;
    }

    private MainPage(boolean isAdmin){
        super();
        title = Titles.MAIN.getTitle();
    }

}
