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

    public Function<Message, List<OSBBSendMessage>> createAppeal(SendMessageParams sendMessageParam, String messageStr, AppealTypes appealTypes) {
        return new Function<Message, List<OSBBSendMessage>>() {
            @Override
            public List<OSBBSendMessage> apply(Message message) {
                AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(miyDimService.getCookie(sendMessageParam.getChatIdAsString()));
                List<OSBBSendMessage> osbbSendMessages = new ArrayList<>();
                if (arrearsMiyDim.isLogin()) {
                    try {

                        chanelMessengerService.sendMessageAppealToBord(sendMessageParam, messageStr, appealTypes);
                        Optional<String> link = arrearsMiyDim.createAppeal(generateStrMessage(messageStr, appealTypes));
                        if (link.isPresent()) {
                            OSBBSendMessage osbbSendMessage = generateAppealBotMessage(sendMessageParam, appealTypes);
                            osbbSendMessages.add(osbbSendMessage);

                            osbbSendMessage = sendMessageBuilder.generateMiyDimAppealMessage(sendMessageParam, link.get());
                            osbbSendMessages.add(osbbSendMessage);
                        } else {
                            osbbSendMessages.add(sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_ERROR_REQUEST_DATA_FOR_MYIDIM.getMessage()));
                        }
                    } catch (UnsupportedEncodingException | URISyntaxException e) {
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    osbbSendMessages.add(sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam));
                }
                return osbbSendMessages;
            }
        };
    }

    private String generateStrMessage(String message, AppealTypes appealTypes) {
        if (appealTypes == AppealTypes.URGENT) {
            message = "ТЕРМІНОВО!!!\n" + message;
        }
        return message;
    }

    private OSBBSendMessage generateAppealBotMessage(SendMessageParams sendMessageParam, AppealTypes appealTypes) throws UnsupportedEncodingException, URISyntaxException {
        if (appealTypes == AppealTypes.URGENT) {
            return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_URGENT_REQUEST_DATA_FOR_MYIDIM.getMessage());
        }
        return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
    }

}
