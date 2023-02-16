package com.osbblevymista.telegram.pages.appeals;

import com.osbblevymista.telegram.pages.BasePage;
import com.osbblevymista.telegram.system.Titles;

public class AppealPage extends BasePage {
    private static AppealPage appealPage = null;

    public static AppealPage getInstance(boolean isAdmin){
        if (appealPage == null){
            appealPage = new AppealPage();
        }
        appealPage.setAdmin(isAdmin);

        return appealPage;
    }

    private AppealPage(){
        super();
        title = Titles.APPEAL.getTitle();
    }

}
