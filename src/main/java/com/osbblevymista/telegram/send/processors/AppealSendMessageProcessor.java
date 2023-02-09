package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.api.services.MiyDimService;
import com.osbblevymista.telegram.keyabords.SettingsKeyboard;
import com.osbblevymista.telegram.miydim.AppealMiyDimProcessor;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static com.osbblevymista.telegram.system.Actions.BUTTON_BACK;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup.*;

@Component
@RequiredArgsConstructor
public class AppealSendMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(AppealSendMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final MiyDimService miyDimService;
    private final BoardProcessor boardProcessor;

    public List<Function<Message, OSBBSendMessage>> createAppeal(SendMessageParams sendMessageParam, String message, AppealTypes appealTypes) throws IOException, URISyntaxException {
        if (AppealTypes.SIMPLE == appealTypes){
            return createAppeal(sendMessageParam, message);
        } else if (AppealTypes.URGENT == appealTypes){
            return createUrgentAppeal(sendMessageParam, message);
        }
        return null;
    }

    public List<Function<Message, OSBBSendMessage>> createUrgentAppeal(SendMessageParams sendMessageParam, String message) throws IOException, URISyntaxException {
        message = "ТЕРМІНОВО!!!\n" + message;
        return createAppeal(sendMessageParam, message);
    }

    private List<Function<Message, OSBBSendMessage>> createAppeal(SendMessageParams sendMessageParam, String messageStr) throws IOException, URISyntaxException {
            List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
            list.add(new Function<Message, OSBBSendMessage>() {
                @Override
                public OSBBSendMessage apply(Message message) {
                    AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(miyDimService.getCookie(sendMessageParam.getChatIdAsString()));
                    if (arrearsMiyDim.isLogin()) {
                        try {
                            boolean response = arrearsMiyDim.createAppeal(messageStr);
                            if (response) {
                                return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
                            } else {
                                return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_ERROR_REQUEST_DATA_FOR_MYIDIM.getMessage());
                            }
                        } catch (UnsupportedEncodingException | URISyntaxException e) {
                            e.printStackTrace();
                            logger.error(e.getMessage(), e);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        OSBBSendMessage sendMessage = sendMessageBuilder.createBaseMessage(sendMessageParam.getChatId());

                        List<InlineKeyboardButton> inlineKeyboardButtons = new ArrayList<>();
                        inlineKeyboardButtons.add(SettingsKeyboard.generateLoginButton(
                                sendMessageParam.getClientIp(),
                                sendMessageParam.getClientPort(),
                                sendMessageParam.getChatId().toString()
                        ));

                        InlineKeyboardMarkupBuilder inlineKeyboardMarkupBuilder = InlineKeyboardMarkup.builder();
                        sendMessage.setReplyMarkup(inlineKeyboardMarkupBuilder
                                .keyboardRow(inlineKeyboardButtons)
                                .build());
                        sendMessage.setText(Messages.MISSING_COOKIE.getMessage());
                        return sendMessage;
                    }
                    return null;
                }
            });
            list.add(boardProcessor.createBoardNotification(sendMessageParam, messageStr));
            return list;
    }

    private List<Function<Message, OSBBSendMessage>> createResponse(SendMessageParams sendMessageParam, String text) throws UnsupportedEncodingException, URISyntaxException {

        List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
        list.add(new Function<Message, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(Message message) {
                try {
                    return sendMessageBuilder.createSimpleMessage(sendMessageParam, text);
                } catch (UnsupportedEncodingException | URISyntaxException e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(),e);
                }
                return null;
            }
        });

        return list;
    }

}
