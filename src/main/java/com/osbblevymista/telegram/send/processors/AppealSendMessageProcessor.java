package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.api.services.MiyDimService;
import com.osbblevymista.telegram.miydim.AppealMiyDimProcessor;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class AppealSendMessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(AppealSendMessageProcessor.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final MiyDimService miyDimService;
    private final ChanelMessengerService chanelMessengerService;


    public List<Function<Message, OSBBSendMessage>> createAppeal(SendMessageParams sendMessageParam, String messageStr, AppealTypes appealTypes) throws IOException, URISyntaxException {
            List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
            list.add(new Function<Message, OSBBSendMessage>() {
                @Override
                public OSBBSendMessage apply(Message message) {
                    AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(miyDimService.getCookie(sendMessageParam.getChatIdAsString()));
                    if (arrearsMiyDim.isLogin()) {
                        try {
                            chanelMessengerService.sendMessageToBord(sendMessageParam, messageStr);
                            Optional<String> link = arrearsMiyDim.createAppeal(generateStrMessage(messageStr, appealTypes));
                            if (link.isPresent()) {
                                return generateAppealBotMessage(sendMessageParam, appealTypes, link.get());
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
                        return sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam);
                    }
                    return null;
                }
            });
            return list;
    }

    private String generateStrMessage(String message, AppealTypes appealTypes){
        if (appealTypes == AppealTypes.URGENT) {
            message = "ТЕРМІНОВО!!!\n" + message;
        }
        return message;
    }

    private OSBBSendMessage generateAppealBotMessage(SendMessageParams sendMessageParam, AppealTypes appealTypes, String link) throws UnsupportedEncodingException, URISyntaxException {
        if (appealTypes == AppealTypes.SIMPLE) {
//            return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
            return sendMessageBuilder.generateMiyDimAppealMessage(
                    sendMessageParam,
                    Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage(),
                    link);
        } else if (appealTypes == AppealTypes.URGENT){
            return sendMessageBuilder.generateMiyDimAppealMessage(
                    sendMessageParam,
                    Messages.RESPONSE_URGENT_REQUEST_DATA_FOR_MYIDIM.getMessage(),
                    link
            );
        } else {
            return null;
        }
    }

}
