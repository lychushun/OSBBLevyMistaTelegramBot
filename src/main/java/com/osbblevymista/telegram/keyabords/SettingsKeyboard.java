package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.api.services.MiyDimService;
import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;

public class SettingsKeyboard extends OSBBKeyboard {

    final Logger logger = LoggerFactory.getLogger(SettingsKeyboard.class);

    @Setter
    private MiyDimService miyDimService;

    private final OSBBKeyboardButton osbbKeyboardButtonVisitMiyDim = new OSBBKeyboardButton(Actions.BUTTON_VISIT_MIYDIM.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonExitMiyDim = new OSBBKeyboardButton(Actions.BUTTON_EXIT_MIYDIM.getText());

    {

        osbbKeyboardButtonVisitMiyDim.setId(Actions.BUTTON_VISIT_MIYDIM.getText());
        osbbKeyboardButtonVisitMiyDim.setOnClickListener(new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.setTitle("Авторизація в МійДім");
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextCell(
                        generateLoginButton(keyboardParam.getClientIp(),
                                keyboardParam.getClientPort(),
                                keyboardParam.getChatId()
                        ));

                return executorListenerResponse;
            }
        });
        insertIntoFirstRow(osbbKeyboardButtonVisitMiyDim);

        osbbKeyboardButtonExitMiyDim.setId(Actions.BUTTON_EXIT_MIYDIM.getText());
        osbbKeyboardButtonExitMiyDim.setOnClickListener(new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) {

                if (miyDimService != null) {
                    miyDimService.deleteCookie(keyboardParam.getChatId());
                    logger.info("Was removed cookie for: " + keyboardParam.getChatId());
                }

                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.messages.add(Messages.LOG_OUT_MIYDIM.getMessage());
                return executorListenerResponse;
            }
        });
        insertIntoFirstRow(osbbKeyboardButtonExitMiyDim);

    }

    public static OSBBInlineKeyboardButton generateLoginButton(String clientIp, String clientPort, String chatId) {
        OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton();
        osbbInlineKeyboardButton.setId("miydimWeb");
        osbbInlineKeyboardButton.setText("МійДім - Авторизація");
        osbbInlineKeyboardButton.setUrl("http://" + clientIp + ":" + clientPort + "?chatId=" + chatId);
        return osbbInlineKeyboardButton;
    }

    public static OSBBInlineKeyboardButton generateOrderButton(String url) {
        OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton();
        osbbInlineKeyboardButton.setId("Звернення");
        osbbInlineKeyboardButton.setText("МійДім - Звернення");
        osbbInlineKeyboardButton.setUrl(url);
        return osbbInlineKeyboardButton;
    }

    public static OSBBInlineKeyboardButton generateHomeButton() {
        OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton();
        osbbInlineKeyboardButton.setId("Звернення");
        osbbInlineKeyboardButton.setText("Головна");
        osbbInlineKeyboardButton.setCallbackData("/main");
        return osbbInlineKeyboardButton;
    }

    public SettingsKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
