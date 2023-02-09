package com.osbblevymista.telegram.pages;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.keyabords.SettingsKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.messages.ArrearsMessages;
import com.osbblevymista.telegram.miydim.ArrearsMiyDimProcessor;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.Messages;
import com.osbblevymista.telegram.system.Titles;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isEmpty;

public class ArrearsPage extends BasePage {

    private static ArrearsPage arrearsPage;

    public static ArrearsPage getInstance(boolean isAdmin) {
        if (arrearsPage == null) {
            arrearsPage = new ArrearsPage();
        }
        arrearsPage.setAdmin(isAdmin);

        return arrearsPage;
    }

    {
        super.messages.add(Messages.GETTING_ARREARS_DATA_FROM_MYIDIM.getMessage());
    }

    @Override
    public void doExecute() {
    }

    @Override
    public boolean doExecute(PageParams sendMessageParams, Function<ExecutorListenerResponse, Boolean> consumer) throws UnsupportedEncodingException, URISyntaxException {

        ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();

        if (isEmpty(sendMessageParams.getCookie())) {
            executorListenerResponse = notLoginResponse(
                    sendMessageParams.getClientIp(),
                    sendMessageParams.getClientPort(),
                    sendMessageParams.getChatId().toString(),
                    null
            );
        } else {
            ArrearsMiyDimProcessor arrearsMiyDim = new ArrearsMiyDimProcessor(sendMessageParams.getCookie());
            if (!arrearsMiyDim.isLogin()) {
                executorListenerResponse = notLoginResponse(
                        sendMessageParams.getClientIp(),
                        sendMessageParams.getClientPort(),
                        sendMessageParams.getChatId().toString(),
                        arrearsMiyDim.getErrorMessage()
                );
            } else {
                ArrearsMessages arrearsMessages = new ArrearsMessages(arrearsMiyDim);
                executorListenerResponse.messages.add(arrearsMessages.getMessage(ArrearsMessages.ARREARS.ARREARS.getValue()));

                OSBBInlineKeyboardButton osbbInlineKeyboardButton = new OSBBInlineKeyboardButton(Actions.BUTTON_PAYMENT_PRIVAT24.getText(), arrearsMiyDim.getPrivat24PaymentLink());
                osbbInlineKeyboardButton.setId(Actions.BUTTON_PAYMENT_PRIVAT24.getText());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

                osbbInlineKeyboardButton = new OSBBInlineKeyboardButton(Actions.BUTTON_PAYMENT_IPAY.getText(), arrearsMiyDim.getIPayPaymentLink());
                osbbInlineKeyboardButton.setId(Actions.BUTTON_PAYMENT_IPAY.getText());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);
            }
        }

        return consumer.apply(executorListenerResponse);
    }

    public static ExecutorListenerResponse notLoginResponse(String clientId, String clientPort, String chatId, String message){
        ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
        if (message != null){
            executorListenerResponse.messages.add(message);
        }

        executorListenerResponse.messages.add(Messages.MISSING_COOKIE.getMessage());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextCell(
                SettingsKeyboard.generateLoginButton(
                        clientId,
                        clientPort,
                        chatId.toString()
                )
        );
        return executorListenerResponse;
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    private ArrearsPage() {
        super();
        title = Titles.ARREARS.getTitle();
    }

}
