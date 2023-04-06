package com.osbblevymista.telegram.keyabords.appeals;

import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.KeyboardParam;
import com.osbblevymista.telegram.keyabords.OSBBKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.miydim.Appeal;
import com.osbblevymista.telegram.miydim.AppealMiyDimProcessor;
import com.osbblevymista.telegram.pages.ArrearsPage;
import com.osbblevymista.telegram.pages.appeals.SubmitSimpleAppealPage;
import com.osbblevymista.telegram.pages.appeals.SubmitUrgentAppealPage;
import com.osbblevymista.telegram.system.Actions;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Links;
import com.osbblevymista.telegram.system.Messages;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;

import static com.osbblevymista.telegram.system.Actions.BUTTON_APPEAL_REVIEW;
import static com.osbblevymista.telegram.system.Commands.SIMPLE_APPEAL;
import static com.osbblevymista.telegram.system.Commands.URGENT_APPEAL;
import static com.osbblevymista.telegram.system.Messages.INSERT_SIMPLE_REQUEST_DATA_FOR_MYIDIM;
import static org.apache.commons.lang3.StringUtils.isEmpty;

public class AppealKeyboard extends OSBBKeyboard {

    private final OSBBKeyboardButton osbbKeyboardButtonCreateSimple = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonCreateUrgent = new OSBBKeyboardButton(Actions.BUTTON_APPEAL_URGENT_CREATE.getText());
    private final OSBBKeyboardButton osbbKeyboardButtonGet = new OSBBKeyboardButton(BUTTON_APPEAL_REVIEW.getText());

    private final OSBBKeyboardButton osbbKeyboardButtonBarrier = new OSBBKeyboardButton(Actions.BUTTON_BARRIER.getText());

    {
        osbbKeyboardButtonCreateSimple.setId(Actions.BUTTON_APPEAL_SIMPLE_CREATE.getText());
        osbbKeyboardButtonCreateSimple.setOnClickListener(getOsbbExecutorListenerCreate(AppealTypes.SIMPLE));
        insertIntoFirstRow(osbbKeyboardButtonCreateSimple);

        osbbKeyboardButtonCreateUrgent.setId(Actions.BUTTON_APPEAL_URGENT_CREATE.getText());
        osbbKeyboardButtonCreateUrgent.setOnClickListener(getOsbbExecutorListenerCreate(AppealTypes.URGENT));
        insertIntoFirstRow(osbbKeyboardButtonCreateUrgent);

        OSBBExecutorListener osbbExecutorListenerGet = new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();

                AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(keyboardParam.getMiyDimCookie());
                if (arrearsMiyDim.isLogin()) {
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
                    return ArrearsPage.notLoginResponse(
                            keyboardParam.getClientIp(),
                            keyboardParam.getClientPort(),
                            keyboardParam.getChatId(),
                            arrearsMiyDim.getErrorMessage()
                    );
                }

                return executorListenerResponse;
            }
        };

        osbbKeyboardButtonGet.setId(BUTTON_APPEAL_REVIEW.getText());
        osbbKeyboardButtonGet.messages.add(Messages.GET_REQUEST_DATA_FOR_MYIDIM.getMessage());
        osbbKeyboardButtonGet.setOnClickListener(osbbExecutorListenerGet);
        insertIntoFirstRow(osbbKeyboardButtonGet);


        osbbExecutorListenerGet = new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws IOException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
                executorListenerResponse.messages.add(Messages.POLL.getMessage());

                OSBBInlineKeyboardButton osbbInlineKeyboardButton =
                        new OSBBInlineKeyboardButton(Actions.BUTTON_CAR_ENTRANCE.getText(), Links.CAR_ENTRANCE.getLink());
                osbbInlineKeyboardButton.setId(Actions.BUTTON_CAR_ENTRANCE.getText());
                executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

                return executorListenerResponse;
            }
        };

        osbbKeyboardButtonBarrier.setId(Actions.BUTTON_BARRIER.getText());
        osbbKeyboardButtonBarrier.setOnClickListener(osbbExecutorListenerGet);
        insertIntoSecondRow(osbbKeyboardButtonBarrier);
    }

    public void setSubmitSimplePage(SubmitSimpleAppealPage submitSimpleAppealPage) {
        osbbKeyboardButtonCreateSimple.setNextPage(submitSimpleAppealPage);
    }

    public void setSubmitUrgentPage(SubmitUrgentAppealPage submitUrgentAppealPage) {
        osbbKeyboardButtonCreateUrgent.setNextPage(submitUrgentAppealPage);
    }

    public AppealKeyboard(boolean isAdmin) {
        super(isAdmin);
    }

    @Override
    public KeyboardButton getBack() {
        return null;
    }

    private OSBBExecutorListener getOsbbExecutorListenerCreate(AppealTypes appealTypes) {
        return new OSBBExecutorListener() {
            @Override
            public ExecutorListenerResponse doExecute(KeyboardParam keyboardParam) throws UnsupportedEncodingException, URISyntaxException {
                ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();

                String command = appealTypes == AppealTypes.SIMPLE ? SIMPLE_APPEAL.getCommand() : URGENT_APPEAL.getCommand();

                if (isEmpty(keyboardParam.getMiyDimCookie())) {
                    executorListenerResponse = ArrearsPage.notLoginResponse(
                            keyboardParam.getClientIp(),
                            keyboardParam.getClientPort(),
                            keyboardParam.getChatId().toString(),
                            null,
                            command
                    );
                } else {
                    if (appealTypes == AppealTypes.SIMPLE) {
                        executorListenerResponse.messages.add(INSERT_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
                    } else if (appealTypes == AppealTypes.URGENT) {
                        executorListenerResponse.messages.add(Messages.INSERT_URGENT_REQUEST_DATA_FOR_MYIDIM.getMessage());
                    }
                }

                return executorListenerResponse;
            }
        };
    }

}
