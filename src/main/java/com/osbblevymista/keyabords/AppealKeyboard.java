package com.osbblevymista.keyabords;

import com.osbblevymista.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.executorlistener.OSBBExecutorListener;
import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.miydim.Appeal;
import com.osbblevymista.miydim.AppealMiyDimProcessor;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Messages;
import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

public class AppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonCreateSimple = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonCreateUrgent = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_URGENT_CREATE.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonGet = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_REVIEW.getText());

    {
        osbbKeyboardButtonCreateSimple.setId(Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText());
        osbbKeyboardButtonCreateSimple.setOsbbExecutorListener(getOsbbExecutorListenerCreate(OSBBExecutorListenerCreatorType.SIMPLE));
        insertIntoFirstRow(osbbKeyboardButtonCreateSimple);


        osbbKeyboardButtonCreateUrgent.setId(Actions.BUTTON_APPEAL_URGENT_CREATE.getText());
        osbbKeyboardButtonCreateUrgent.setOsbbExecutorListener(getOsbbExecutorListenerCreate(OSBBExecutorListenerCreatorType.URGENT));
        insertIntoFirstRow(osbbKeyboardButtonCreateUrgent);


        OSBBExecutorListener osbbExecutorListenerGet = new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();

                AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(keyboardParam.getLogin(), keyboardParam.getPass());
                if (arrearsMiyDim.getIsLogin()) {
                    List<Appeal> appealList = arrearsMiyDim.getAppeals();
                    appealList.forEach(item -> {
                        StringBuilder stringBuffer = new StringBuilder();
                        stringBuffer
                                .append("<b>Дата створення: </b>")
                                .append(item.getCreateDateTime())
                                .append("\n")
                                .append("<b>Повідомлення: </b>").append(item.getMessage())
                                .append("\n")
                                .append("<b>Статус: </b>").append(item.getAppeal_status().toString())
                                .append("\n");
                        executorListenerResponse.messages.add(stringBuffer.toString());
                    });
                } else {
                    executorListenerResponse.messages.add(
                            arrearsMiyDim.getErrorMessage() + "\n" +
                                    Messages.MISSING_LOG_AND_PASS.getMessage() + "\n");
                }

                return executorListenerResponse;
            }
        };

        osbbKeyboardButtonGet.setId(Actions.BUTTON_APPEAL_REVIEW.getText());
        osbbKeyboardButtonGet.messages.add(Messages.GET_REQUEST_DATA_FOR_MYIDIM.getMessage());
        osbbKeyboardButtonGet.setOsbbExecutorListener(osbbExecutorListenerGet);
        insertIntoFirstRow(osbbKeyboardButtonGet);

    }

    public AppealKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

    private OSBBExecutorListener getOsbbExecutorListenerCreate(OSBBExecutorListenerCreatorType osbbExecutorListenerCreatorType) {
        return new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws UnsupportedEncodingException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();

                if (StringUtils.isEmpty(keyboardParam.getLogin()) || StringUtils.isEmpty(keyboardParam.getPass())) {
                    executorListenerResponse.messages.add(Messages.MISSING_LOG_AND_PASS.getMessage());
                } else {
                    if (osbbExecutorListenerCreatorType == OSBBExecutorListenerCreatorType.SIMPLE) {
                        executorListenerResponse.messages.add(Messages.INSERT_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
                    } else if (osbbExecutorListenerCreatorType == OSBBExecutorListenerCreatorType.URGENT) {
                        executorListenerResponse.messages.add(Messages.INSERT_URGENT_REQUEST_DATA_FOR_MYIDIM.getMessage());
                    }
                }

                return executorListenerResponse;
            }
        };
    }

    private enum OSBBExecutorListenerCreatorType {
        SIMPLE,
        URGENT
    }
}
