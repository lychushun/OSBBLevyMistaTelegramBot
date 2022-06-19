package com.osbblevymista.pages;

import com.osbblevymista.system.Titles;

public class ContactPage extends BasePage{

    private static ContactPage contactPage = null;

    public static ContactPage getInstance(boolean isAdmin){
        if (contactPage == null){
            contactPage = new ContactPage();
        }
        contactPage.setAdmin(isAdmin);

        return contactPage;
    }

    private ContactPage(){
        super();
        title = Titles.CHATS.getTitle();
    }

}
