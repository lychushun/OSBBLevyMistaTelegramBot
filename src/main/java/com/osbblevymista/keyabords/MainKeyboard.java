package com.osbblevymista.keyabords;

import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class MainKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonInfo = new OSBBKeyboardButton(BUTTONS.INFO.text);
    private final OSBBKeyboardButton osbbKeyboardButtonArrears = new OSBBKeyboardButton(BUTTONS.ARREARS.text);
    private final OSBBKeyboardButton osbbKeyboardButtonSettings = new OSBBKeyboardButton(BUTTONS.SETTINGS.text);
    private final OSBBKeyboardButton osbbKeyboardButtonChats = new OSBBKeyboardButton(BUTTONS.CHATS.text);
    private final OSBBKeyboardButton osbbKeyboardButtonAdmin = new OSBBKeyboardButton(BUTTONS.ADMIN.text);
    private final OSBBKeyboardButton osbbKeyboardButtonAppeal = new OSBBKeyboardButton(BUTTONS.APPEAL.text);

    {

        osbbKeyboardButtonArrears.setId(BUTTONS.ARREARS.text);
        insertIntoFirstRow(osbbKeyboardButtonArrears);

        osbbKeyboardButtonAppeal.setId(BUTTONS.APPEAL.text);
        insertIntoFirstRow(osbbKeyboardButtonAppeal);

        osbbKeyboardButtonInfo.setId(BUTTONS.INFO.text);
        insertIntoSecondRow(osbbKeyboardButtonInfo);

        osbbKeyboardButtonChats.setId(BUTTONS.CHATS.text);
        insertIntoSecondRow(osbbKeyboardButtonChats);

        osbbKeyboardButtonSettings.setId(BUTTONS.SETTINGS.text);
        insertIntoThirdRow(osbbKeyboardButtonSettings);

        osbbKeyboardButtonAdmin.setId(BUTTONS.ADMIN.text);
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

    @AllArgsConstructor
    @Getter
    public enum BUTTONS {
        CONTACT("Контакти \uD83D\uDE01"),
        INFO("Інформація"),
        ARREARS("Заборгованість \uD83D\uDCB0"),
        SETTINGS("Налаштування \uD83D\uDEA9"),
        CHATS("Чати \uD83D\uDCDA"),
        ADMIN("Адмін \uD83D\uDC77"),
        APPEAL("Звернення \uD83D\uDE4B"),
        REPORTS("Звіти та документи \uD83D\uDCC4");

        String text;
    }

}
