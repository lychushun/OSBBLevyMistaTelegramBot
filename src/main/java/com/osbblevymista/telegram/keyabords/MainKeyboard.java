package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.*;
import com.osbblevymista.telegram.pages.appeals.AppealPage;
import com.osbblevymista.telegram.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonInfo = new OSBBKeyboardButton(Actions.BUTTON_INFO.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonArrears = new OSBBKeyboardButton(Actions.BUTTON_ARREARS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonSettings = new OSBBKeyboardButton(Actions.BUTTON_SETTINGS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonChats = new OSBBKeyboardButton(Actions.BUTTON_CHATS.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAdmin = new OSBBKeyboardButton(Actions.BUTTON_ADMIN.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonAppeal = new OSBBKeyboardButton(Actions.BUTTON_APPEAL.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonVisitMiyDim = new OSBBKeyboardButton(Actions.BUTTON_VISIT_MIYDIM.getText());

    {

        osbbKeyboardButtonArrears.setId(Actions.BUTTON_ARREARS.getText());
        insertIntoFirstRow(osbbKeyboardButtonArrears);

        osbbKeyboardButtonAppeal.setId(Actions.BUTTON_APPEAL.getText());
        insertIntoFirstRow(osbbKeyboardButtonAppeal);

        osbbKeyboardButtonVisitMiyDim.setId(Actions.BUTTON_VISIT_MIYDIM.getText());
        osbbKeyboardButtonVisitMiyDim.setOsbbExecutorListener(new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.setTitle("miydimWebT");
//                executorListenerResponse.messages.add("<p>aaa</p>");
//                executorListenerResponse.setParseMode(MessagesParseMode.HTML);
                OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton();
                osbbInlineKeyboardButton.setId("miydimWeb");
                osbbInlineKeyboardButton.setText("miydimWebM");

//                osbbInlineKeyboardButton.setText("miydimWeb");
                osbbInlineKeyboardButton.setUrl("http://"+ getClientIp() + ":" + getClientPort() + "/TelegramBot/src/main/web_pages/index.html?chatId=" + keyboardParam.chatId);
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextCell(osbbInlineKeyboardButton);
                return executorListenerResponse;
            }
        });
        insertIntoFirstRow(osbbKeyboardButtonVisitMiyDim);

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
