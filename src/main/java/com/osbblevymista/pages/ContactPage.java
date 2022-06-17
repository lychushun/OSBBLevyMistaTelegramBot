package com.osbblevymista.pages;

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
        title = "Контакти";
    }

}
