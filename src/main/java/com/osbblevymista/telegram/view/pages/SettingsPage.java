package com.osbblevymista.telegram.view.pages;

import com.osbblevymista.telegram.system.Titles;

public class SettingsPage extends BasePage{

    private static SettingsPage settings = null;

    public static SettingsPage getInstance(boolean isAdmin){
        if (settings == null){
            settings = new SettingsPage(isAdmin);
        }
        settings.setAdmin(isAdmin);

        return settings;
    }

    private SettingsPage(boolean isAdmin){
        super();
        title = Titles.SETTINGS.getTitle();
    }

}
