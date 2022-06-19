package com.osbblevymista.keyabords;

import com.osbblevymista.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.executorlistener.OSBBExecutorListener;
import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.pages.ArrearsPage;
import com.osbblevymista.pages.ChatPage;
import com.osbblevymista.send.processors.SendingMessageProcessor;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

public class AdminKeyboard extends OSBBKeyboard{

    private final OSBBKeyboardButton osbbKeyboardButtonSend = new OSBBKeyboardButton(Actions.BUTTON_ADMIN_SEND.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonNewReceipt = new OSBBKeyboardButton(Actions.BUTTON_ADMIN_NEW_RECEIPT.getText());

//    private final SendingMessageProcessor sendingMessageProcessor;

    {
        OSBBExecutorListener osbbExecutorListenerSend = new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.messages.add(Messages.SENT_MESSAGE_TO_ALL.toString());
                return executorListenerResponse;
            }
        };

        osbbKeyboardButtonSend.setId(Actions.BUTTON_ADMIN_SEND.getText());
        osbbKeyboardButtonSend.messages.add(Messages.GET_REQUEST_DATA_FOR_MYIDIM.getMessage());
        osbbKeyboardButtonSend.setOsbbExecutorListener(osbbExecutorListenerSend);
        insertIntoFirstRow(osbbKeyboardButtonSend);

        osbbKeyboardButtonNewReceipt.setId(Actions.BUTTON_ADMIN_NEW_RECEIPT.getText());
        insertIntoSecondRow(osbbKeyboardButtonNewReceipt);
    }

    public AdminKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

}
