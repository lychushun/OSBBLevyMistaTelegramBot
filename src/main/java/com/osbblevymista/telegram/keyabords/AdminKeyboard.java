package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.ContactPage;
import com.osbblevymista.telegram.pages.SendMessagePage;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class AdminKeyboard extends OSBBKeyboard{

    private final OSBBKeyboardButton osbbKeyboardButtonSend = new OSBBKeyboardButton(Actions.BUTTON_ADMIN_SEND.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonNewReceipt = new OSBBKeyboardButton(Actions.BUTTON_ADMIN_NEW_RECEIPT.getText());

    {
//        OSBBExecutorListener osbbExecutorListenerSend = new OSBBExecutorListener() {
//            @Override
//            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) {
//                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
//                executorListenerResponse.messages.add(Messages.SENT_MESSAGE_TO_ALL.getMessage());
//                return executorListenerResponse;
//            }
//        };

        osbbKeyboardButtonSend.setId(Actions.BUTTON_ADMIN_SEND.getText());
        osbbKeyboardButtonSend.messages.add(Messages.SENT_MESSAGE_TO_ALL.getMessage());
//        osbbKeyboardButtonSend.setOsbbExecutorListener(osbbExecutorListenerSend);
        insertIntoFirstRow(osbbKeyboardButtonSend);

        osbbKeyboardButtonNewReceipt.setId(Actions.BUTTON_ADMIN_NEW_RECEIPT.getText());
        insertIntoSecondRow(osbbKeyboardButtonNewReceipt);
    }

    public void setSendMessagePage(SendMessagePage sendMessagePage) {
        osbbKeyboardButtonSend.setNextPage(sendMessagePage);
    }

    public AdminKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
