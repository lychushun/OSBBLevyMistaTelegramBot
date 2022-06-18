package com.osbblevymista.keyabords;

import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.*;
import com.osbblevymista.system.Actions;
import lombok.AllArgsConstructor;
import lombok.Getter;
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

//    @AllArgsConstructor
//    @Getter
//    public enum BUTTONS {
//        CONTACT("Контакти \uD83D\uDE01"),
//        INFO("Інформація \u2139"),
//        ARREARS("Заборгованість \uD83D\uDCB0"),
//        SETTINGS("Налаштування \uD83D\uDEA9"),
//        CHATS("Чати \uD83D\uDCDA"),
//        ADMIN("Адмін \uD83D\uDC77"),
//        APPEAL("Звернення \uD83D\uDE4B"),
//        REPORTS("Звіти та документи \uD83D\uDCC4");
//
//        String text;
//    }

}
