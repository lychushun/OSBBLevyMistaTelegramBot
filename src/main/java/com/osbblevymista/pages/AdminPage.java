package com.osbblevymista.pages;

public class AdminPage extends BasePage{
    private static AdminPage adminPage = null;

    public static AdminPage getInstance(boolean isAdmin){
        if (adminPage == null){
            adminPage = new AdminPage();
        }
        adminPage.setAdmin(isAdmin);

        return adminPage;
    }

    private AdminPage(){
        super();
        title = "Чати";
    }
}
