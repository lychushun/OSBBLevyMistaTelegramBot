package com.osbblevymista.telegram.pages;

import com.osbblevymista.telegram.system.Titles;

public class ContactPage2 extends BasePage{

    private static ContactPage2 contactPage = null;

    public static ContactPage2 getInstance(boolean isAdmin){
        if (contactPage == null){
            contactPage = new ContactPage2();
        }
        contactPage.setAdmin(isAdmin);

        return contactPage;
    }

    private ContactPage2(){
        super();
        title = Titles.CONTACTS.getTitle();
    }

}
