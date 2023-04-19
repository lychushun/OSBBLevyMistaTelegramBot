package com.osbblevymista.telegram.send.processors;

import com.osbblevymista.miydim.services.MiyDimService;
import com.osbblevymista.telegram.dto.telegrammessage.StrTelegramMessage;
import com.osbblevymista.telegram.dto.telegrammessage.TelegramMessage;
import com.osbblevymista.miydim.AppealMiyDimProcessor;
import com.osbblevymista.telegram.send.messages.OSBBStrMessage;
import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import com.osbblevymista.telegram.services.ChanelMessengerService;
import com.osbblevymista.telegram.system.AppealTypes;
import com.osbblevymista.telegram.system.Messages;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class AppealSendMessageProcessor extends MessageProcessor {

    private final Logger logger = LoggerFactory.getLogger(AppealSendMessageProcessor.class);

    @Autowired
    private SendMessageBuilder sendMessageBuilder;

    @Autowired
    private MiyDimService miyDimService;

    @Autowired
    private ChanelMessengerService chanelMessengerService;

    public Function<Message, List<PartialBotApiMethod<Message>>> createAppeal(SendMessageParams sendMessageParam, List<TelegramMessage> messages, AppealTypes appealTypes) {
        return new Function<Message, List<PartialBotApiMethod<Message>>>() {
            @Override
            public List<PartialBotApiMethod<Message>> apply(Message message) {
                AppealMiyDimProcessor arrearsMiyDim = new AppealMiyDimProcessor(miyDimService.getCookie(sendMessageParam.getChatIdAsString()));
                List<PartialBotApiMethod<Message>> osbbSendMessages = new ArrayList<>();
                if (arrearsMiyDim.isLogin()) {
                    try {
                        chanelMessengerService.sendMessageAppealToBord(sendMessageParam, messages, appealTypes);

                        Optional<String> link = arrearsMiyDim.createAppeal(generateAppealStrMessage(messages, appealTypes));
                        if (link.isPresent()) {
                            OSBBStrMessage osbbSendMessage = generateAppealBotMessage(sendMessageParam, appealTypes);
                            osbbSendMessages.add(osbbSendMessage);

                            osbbSendMessage = sendMessageBuilder.generateMiyDimAppealMessage(sendMessageParam, link.get());
                            osbbSendMessages.add(osbbSendMessage);
                        } else {
                            osbbSendMessages.add(sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_ERROR_REQUEST_DATA_FOR_MYIDIM.getMessage()));
                        }
                    } catch (URISyntaxException | IOException e) {
                        logger.error(e.getMessage(), e);
                        e.printStackTrace();
                    }
                } else {
                    osbbSendMessages.add(sendMessageBuilder.generateMiyDimNotLoginMessage(sendMessageParam));
                }
                return osbbSendMessages;
            }
        };
    }

    private String generateAppealStrMessage(List<TelegramMessage> messages, AppealTypes appealTypes) {
        String message = messages
                .stream()
                .filter(el -> el.getType().equals(StrTelegramMessage.TYPE))
                .map(el -> (String)el.getContent())
                .collect(Collectors.joining("\n"));

        if (appealTypes == AppealTypes.URGENT) {
            message = "ТЕРМІНОВО!!!\n" + message;
        }
        return message;
    }

    private OSBBStrMessage generateAppealBotMessage(SendMessageParams sendMessageParam, AppealTypes appealTypes) throws UnsupportedEncodingException, URISyntaxException {
        if (appealTypes == AppealTypes.URGENT) {
            return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_URGENT_REQUEST_DATA_FOR_MYIDIM.getMessage());
        }
        return sendMessageBuilder.createSimpleMessage(sendMessageParam, Messages.RESPONSE_SIMPLE_REQUEST_DATA_FOR_MYIDIM.getMessage());
    }

}
