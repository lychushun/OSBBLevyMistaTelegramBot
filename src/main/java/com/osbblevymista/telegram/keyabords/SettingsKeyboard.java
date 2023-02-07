package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;

public class SettingsKeyboard  extends OSBBKeyboard {


    private final OSBBKeyboardButton osbbKeyboardButtonVisitMiyDim = new OSBBKeyboardButton(Actions.BUTTON_VISIT_MIYDIM.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonExitMiyDim = new OSBBKeyboardButton(Actions.BUTTON_EXIT_MIYDIM.getText());

    {

        osbbKeyboardButtonVisitMiyDim.setId(Actions.BUTTON_VISIT_MIYDIM.getText());
        osbbKeyboardButtonVisitMiyDim.setOsbbExecutorListener(new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.setTitle("Авторизація в МійДім");
                OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton();
                osbbInlineKeyboardButton.setId("miydimWeb");
                osbbInlineKeyboardButton.setText("МійДім");
                osbbInlineKeyboardButton.setUrl("http://"+ keyboardParam.getClientIp() + ":" + keyboardParam.getClientPort() + "?chatId=" + keyboardParam.getChatId());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextCell(osbbInlineKeyboardButton);
                return executorListenerResponse;
            }
        });
        insertIntoFirstRow(osbbKeyboardButtonVisitMiyDim);
        insertIntoFirstRow(osbbKeyboardButtonExitMiyDim);

    }

    public SettingsKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
