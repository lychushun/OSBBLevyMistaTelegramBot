package com.osbblevymista.telegram.view.pages.appeals;

import com.osbblevymista.telegram.view.pages.BasePage;
import com.osbblevymista.telegram.system.Titles;

public class SubmitUrgentAppealPage extends BasePage {
    private static SubmitUrgentAppealPage appealPage = null;

    public static SubmitUrgentAppealPage getInstance(boolean isAdmin){
        if (appealPage == null){
            appealPage = new SubmitUrgentAppealPage();
        }
        appealPage.setAdmin(isAdmin);

        return appealPage;
    }

    private SubmitUrgentAppealPage(){
        super();
        title = Titles.APPEAL_URGENT.getTitle();
    }

}
