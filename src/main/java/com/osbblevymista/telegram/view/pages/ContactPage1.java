package com.osbblevymista.telegram.view.pages;

import com.osbblevymista.telegram.system.Titles;

public class ContactPage1 extends BasePage{

    private static ContactPage1 contactPage = null;

    public static ContactPage1 getInstance(boolean isAdmin){
        if (contactPage == null){
            contactPage = new ContactPage1();
        }
        contactPage.setAdmin(isAdmin);

        return contactPage;
    }

    private ContactPage1(){
        super();
        title = Titles.CONTACTS.getTitle();
    }

}
