package com.osbblevymista.telegram.view.keyabords;

import com.osbblevymista.telegram.view.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.view.pages.*;
import com.osbblevymista.telegram.view.pages.appeals.AppealPage;
import com.osbblevymista.telegram.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class MainKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonInfo = new OSBBKeyboardButton(Actions.BUTTON_INFO.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonArrears = new OSBBKeyboardButton(Actions.BUTTON_ARREARS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonSettings = new OSBBKeyboardButton(Actions.BUTTON_SETTINGS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonChats = new OSBBKeyboardButton(Actions.BUTTON_CHATS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAdmin = new OSBBKeyboardButton(Actions.BUTTON_ADMIN.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAppeal = new OSBBKeyboardButton(Actions.BUTTON_APPEAL.getText());

    {

        osbbKeyboardButtonArrears.setId(Actions.BUTTON_ARREARS.getText());
        insertIntoFirstRow(osbbKeyboardButtonArrears);

        osbbKeyboardButtonAppeal.setId(Actions.BUTTON_APPEAL.getText());
        insertIntoFirstRow(osbbKeyboardButtonAppeal);

        osbbKeyboardButtonInfo.setId(Actions.BUTTON_INFO.getText());
        insertIntoSecondRow(osbbKeyboardButtonInfo);

        osbbKeyboardButtonChats.setId(Actions.BUTTON_CHATS.getText());
        insertIntoSecondRow(osbbKeyboardButtonChats);

        osbbKeyboardButtonSettings.setId(Actions.BUTTON_SETTINGS.getText());
        insertIntoThirdRow(osbbKeyboardButtonSettings);

        osbbKeyboardButtonAdmin.setId(Actions.BUTTON_ADMIN.getText());
        osbbKeyboardButtonAdmin.setOnlyAdmin(true);
        insertIntoThirdRow(osbbKeyboardButtonAdmin);

    }

    public MainKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    public void setInfoPage(InfoPage infoPage) {
        osbbKeyboardButtonInfo.setNextPage(infoPage);
    }

    public void setArrearsPage(ArrearsPage arrearsPage) {
        osbbKeyboardButtonArrears.setNextPage(arrearsPage);
    }

    public void setSettingsPage(SettingsPage settingsPage) {
        osbbKeyboardButtonSettings.setNextPage(settingsPage);
    }

    public void setAppealPage(AppealPage appealPage) {
        osbbKeyboardButtonAppeal.setNextPage(appealPage);
    }

    public void setAdminPage(AdminPage admin) {
        osbbKeyboardButtonAdmin.setNextPage(admin);
    }

    public void setChatPage(ChatPage chatPage) {
        osbbKeyboardButtonChats.setNextPage(chatPage);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
