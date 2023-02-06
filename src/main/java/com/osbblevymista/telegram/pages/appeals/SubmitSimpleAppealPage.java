package com.osbblevymista.telegram.pages.appeals;

import com.osbblevymista.telegram.pages.BasePage;
import com.osbblevymista.telegram.system.Titles;

public class SubmitSimpleAppealPage extends BasePage {
    private static SubmitSimpleAppealPage appealPage = null;

    public static SubmitSimpleAppealPage getInstance(boolean isAdmin){
        if (appealPage == null){
            appealPage = new SubmitSimpleAppealPage();
        }
        appealPage.setAdmin(isAdmin);

        return appealPage;
    }

    private SubmitSimpleAppealPage(){
        super();
        title = Titles.APPEAL_SIMPLE.getTitle();
    }

}
