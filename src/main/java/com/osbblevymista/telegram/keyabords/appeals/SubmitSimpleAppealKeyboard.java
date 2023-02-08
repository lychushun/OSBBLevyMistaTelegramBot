package com.osbblevymista.telegram.keyabords.appeals;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.KeyboardParam;
import com.osbblevymista.telegram.keyabords.OSBBKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.ArrearsPage;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class SubmitSimpleAppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonApprove = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_SIMPLE_APPROVE.getText());

    {
//        OSBBExecutorListener osbbExecutorListenerSend = new OSBBExecutorListener() {
//            @Override
//            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) {
//                ExecutorListenerResponse executorListenerResponse = ArrearsPage.notLoginResponse(
//                        keyboardParam.getClientIp(),
//                        keyboardParam.getClientPort(),
//                        keyboardParam.getChatId(),
//                        null);
//                return executorListenerResponse;
//            }
//        };

//        osbbKeyboardButtonApprove.setOsbbExecutorListener(osbbExecutorListenerSend);
        osbbKeyboardButtonApprove.setId(Actions.BUTTON_APPEAL_SIMPLE_APPROVE.getText());
        insertIntoFirstRow(osbbKeyboardButtonApprove);
    }


    public SubmitSimpleAppealKeyboard(boolean isAdmin) {
        super(isAdmin, "\u200C");
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }
}
