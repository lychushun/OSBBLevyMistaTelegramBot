package com.osbblevymista;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.miydim.services.MiyDimService;
import com.osbblevymista.telegram.botexecution.BotExecution;
import com.osbblevymista.telegram.ApplicationConfig;
import com.osbblevymista.telegram.dto.SendMessageInfo;
import com.osbblevymista.telegram.view.keyabords.*;
import com.osbblevymista.telegram.view.keyabords.appeals.AppealKeyboard;
import com.osbblevymista.telegram.view.keyabords.appeals.SubmitSimpleAppealKeyboard;
import com.osbblevymista.telegram.view.keyabords.appeals.SubmitUrgentAppealKeyboard;
import com.osbblevymista.telegram.view.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.view.pages.*;
import com.osbblevymista.telegram.view.pages.appeals.AppealPage;
import com.osbblevymista.telegram.view.pages.appeals.SubmitSimpleAppealPage;
import com.osbblevymista.telegram.view.pages.appeals.SubmitUrgentAppealPage;
import com.osbblevymista.telegram.send.messages.OSBBStrMessage;
import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
import com.osbblevymista.telegram.send.messages.SendMessageParams;
import com.osbblevymista.telegram.send.processors.ActionSendMessageProcessor;
import com.osbblevymista.telegram.send.processors.SessionSendMessageProcessor;
import com.osbblevymista.telegram.services.AdminInfoService;
import com.osbblevymista.telegram.services.BotExecutionDataService;
import com.osbblevymista.telegram.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import org.thymeleaf.util.ListUtils;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Component
@RequiredArgsConstructor
public class OSBBLevyMistaTelegramLongPollingSessionBot extends TelegramLongPollingSessionBot {

    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    private final SendMessageBuilder sendMessageBuilder;
    private final ActionSendMessageProcessor actionSendMessageProcessor;
    private final SessionSendMessageProcessor sessionSendMessageProcessor;
    private final UserInfoService userInfoService;
    private final AdminInfoService adminInfoService;
    private final MiyDimService miyDimService;
    private final BotExecutionDataService botExecutionDataService;

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${client.ip}")
    private String clientIp;

    @Value("${client.port}")
    private String clientPort;

    public void onUpdateReceived(Update update, Optional<Session> optional) {

        try {

            SendMessageInfo sendMessageInfo = getSendMessageInfo(update);

            if (sendMessageInfo.isNotEmpty()) {
                logger.info(sendMessageInfo.toString());

                Message message = sendMessageInfo.getMessage();
                Long chatId = sendMessageInfo.getChatId();

                BotExecution botExecutionData = new BotExecution();

                SendMessageParams.SendMessageParamsBuilder sendMessageParamsBuilder = getSendMessageParamsBuilder(message, chatId);

                if (sendMessageInfo.isNotEmpty()) {
                    String command = sendMessageInfo.getCommand();

                    if (isNotEmpty(command) && command.equals("/start")) {
                        botExecutionData = botExecutionDataService.generateStartPage(sendMessageParamsBuilder.build(), getPageInfrastructure(
                                adminInfoService.isAdmin(message.getFrom().getId())
                        ));
                    } else if (isNotEmpty(command) && command.equals("/main")) {
                        botExecutionData = botExecutionDataService.generateMainPage(sendMessageParamsBuilder.build(),
                                getPageInfrastructure(
                                        adminInfoService.isAdmin(message.getChatId())
                                ));
                    } else if (isNotEmpty(command) && command.equals("/loginSuccess")) {
                        botExecutionData = botExecutionDataService.generateSuccessLogin(sendMessageParamsBuilder.build(), getPageInfrastructure(
                                adminInfoService.isAdmin(message.getFrom().getId())
                        ));
                    } else {
                        UserInfo userInfo = getUserInfo(message);
                        userInfoService.addRow(userInfo);

                        if (update.hasCallbackQuery()) {
                            Function<CallbackQuery, OSBBStrMessage> newMessage = processCallBack();
                            execute(newMessage.apply(update.getCallbackQuery()));
                        }

                        if (update.hasMessage() || !ListUtils.isEmpty(message.getPhoto())) {
                            sendMessageParamsBuilder
                                    .clientPort(clientPort)
                                    .clientIp(clientIp)
                                    .chatId(chatId)
                                    .build();

                            botExecutionData = sessionSendMessageProcessor.processSession(sendMessageInfo, sendMessageParamsBuilder.build(), optional);

                            if (botExecutionData == null) {
                                OSBBKeyboardButton osbbKeyboardButton = getOSBBKeyboardButton(message);
                                botExecutionData = botExecutionDataService.generatePageOrAction(sendMessageParamsBuilder.build(), osbbKeyboardButton);
                            }
                        }
                    }

                } else {
                    botExecutionData.addExecutionsForMessage(actionSendMessageProcessor.createHomePageMessage(sendMessageParamsBuilder.build()));
                }

                processMultiMessages(botExecutionData, update.getMessage());

            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }


    private SendMessageInfo getSendMessageInfo(Update update) {

        return SendMessageInfo
                .builder()
                .update(update)
                .build();

    }

    @Override
    public void onUpdatesReceived(List<Update> updates) {
        super.onUpdatesReceived(updates);
    }

    @Override
    public String getBotUsername() {
        return name;
    }

    @Override
    public String getBotToken() {
        return token;
    }

    @Override
    public void onRegister() {
        super.onRegister();
    }

    private UserInfo getUserInfo(Message message) {
        UserInfo userInfo = new UserInfo();
        userInfo.setLastName(message.getChat().getLastName());
        userInfo.setFirstName(message.getChat().getFirstName());
        userInfo.setChatId(message.getChat().getId().toString());
        userInfo.setUserId(message.getFrom().getId());
        userInfo.setUserName(message.getFrom().getUserName());
        return userInfo;
    }

    public Function<CallbackQuery, OSBBStrMessage> processCallBack() {
        return new Function<CallbackQuery, OSBBStrMessage>() {
            @Override
            public OSBBStrMessage apply(CallbackQuery callbackQuery) {
                Message sendMessage = callbackQuery.getMessage();
                OSBBStrMessage newMessage = sendMessageBuilder.createEmptyMessage(sendMessage.getChatId());
                newMessage.setText(callbackQuery.getData());
                return newMessage;
            }

        };
    }

    private void processMultiMessages(BotExecution botExecutionData, Message message) {
        botExecutionData.execute(message, (PartialBotApiMethod<Message> el) -> {
                    try {
                        if (el instanceof BotApiMethod<Message>) {
                            execute((BotApiMethod<Message>) el);
                        } else if (el instanceof SendPhoto) {
                            execute((SendPhoto) el);
                        } else if (el instanceof SendDocument) {
                            execute((SendDocument) el);
                        } else if (el instanceof SendVideo) {
                            execute((SendVideo) el);
                        } else if (el instanceof SendAudio) {
                            execute((SendAudio) el);
                        }
                    } catch (TelegramApiException e) {
                        logger.error("Can not execute command");
                        e.printStackTrace();
                    }
                }
        );
    }

    private SendMessageParams.SendMessageParamsBuilder getSendMessageParamsBuilder(Message message, Long chatId) {
        return SendMessageParams
                .builder()
                .firstName(message.getFrom().getFirstName())
                .lastName(message.getFrom().getLastName())
                .userName(message.getFrom().getUserName())
                .userId(message.getFrom().getId())
                .chatId(chatId);
    }

    private OSBBKeyboardButton getOSBBKeyboardButton(Message message) throws IOException, CsvException {
        String buttonText = message.getText();
        MainPage mainPage = getPageInfrastructure(
                adminInfoService.isAdmin(
                        message.getFrom().getId()
                )
        );
        return mainPage.currentButton(buttonText);
    }

    public MainPage getPageInfrastructure(boolean isAdmin) {
        MainPage mainPage = MainPage.getInstance(isAdmin);
        ContactPage1 contactPage1 = ContactPage1.getInstance(isAdmin);
        ContactPage2 contactPage2 = ContactPage2.getInstance(isAdmin);
        ArrearsPage arrearsPage = ArrearsPage.getInstance(isAdmin);
        SettingsPage settingsPage = SettingsPage.getInstance(isAdmin);
        AppealPage appealPage = AppealPage.getInstance(isAdmin);
        SubmitSimpleAppealPage submitSimpleAppealPage = SubmitSimpleAppealPage.getInstance(isAdmin);
        SubmitUrgentAppealPage submitUrgentAppealPage = SubmitUrgentAppealPage.getInstance(isAdmin);
        AdminPage adminPage = AdminPage.getInstance(isAdmin);
        ChatPage chatPage = ChatPage.getInstance(isAdmin);
        InfoPage infoPage = InfoPage.getInstance(isAdmin);
        ReportPage reportPage = ReportPage.getInstance(isAdmin);
        SendMessagePage sendMessagePage = SendMessagePage.getInstance(isAdmin);

        MainKeyboard mainKeyboard = new MainKeyboard(isAdmin);
        mainKeyboard.setArrearsPage(arrearsPage);
        mainKeyboard.setInfoPage(infoPage);
        mainKeyboard.setSettingsPage(settingsPage);
        mainKeyboard.setAppealPage(appealPage);
        mainKeyboard.setAdminPage(adminPage);
        mainKeyboard.setChatPage(chatPage);

        ContactKeyboard1 contactKeyboard1 = new ContactKeyboard1(isAdmin);
        contactKeyboard1.setPrevPage(infoPage);
        contactKeyboard1.setNextPage(contactPage2);

        ContactKeyboard2 contactKeyboard2 = new ContactKeyboard2(isAdmin);
        contactKeyboard2.setPrevPage(contactPage1);

        SettingsKeyboard settingsKeyboard = new SettingsKeyboard(isAdmin);
        settingsKeyboard.setPrevPage(mainPage);
        settingsKeyboard.setMiyDimService(miyDimService);

        AppealKeyboard appealKeyboard = new AppealKeyboard(isAdmin);
        appealKeyboard.setPrevPage(mainPage);
        appealKeyboard.setSubmitSimplePage(submitSimpleAppealPage);
        appealKeyboard.setSubmitUrgentPage(submitUrgentAppealPage);

        AdminKeyboard adminKeyboard = new AdminKeyboard(isAdmin);
        adminKeyboard.setPrevPage(mainPage);
        adminKeyboard.setSendMessagePage(sendMessagePage);

        SendMessageByBoardKeyboard sendMessageByBoardKeyboard = new SendMessageByBoardKeyboard(isAdmin);
        sendMessageByBoardKeyboard.setPrevPage(adminPage);

        InfoKeyboard infoKeyboard = new InfoKeyboard(isAdmin);
        infoKeyboard.setPrevPage(mainPage);
        infoKeyboard.setContactPage(contactPage1);
        infoKeyboard.setReportPage(reportPage);

        SubmitSimpleAppealKeyboard submitSimpleAppealKeyboard = new SubmitSimpleAppealKeyboard(isAdmin);
        submitSimpleAppealKeyboard.setPrevPage(appealPage);

        SubmitUrgentAppealKeyboard submitUrgentAppealKeyboard = new SubmitUrgentAppealKeyboard(isAdmin);
        submitUrgentAppealKeyboard.setPrevPage(appealPage);

        mainPage.setKeyboard(mainKeyboard);
        contactPage1.setKeyboard(contactKeyboard1);
        contactPage2.setKeyboard(contactKeyboard2);
        settingsPage.setKeyboard(settingsKeyboard);
        sendMessagePage.setKeyboard(sendMessageByBoardKeyboard);
        appealPage.setKeyboard(appealKeyboard);
        adminPage.setKeyboard(adminKeyboard);
        infoPage.setKeyboard(infoKeyboard);
        submitSimpleAppealPage.setKeyboard(submitSimpleAppealKeyboard);
        submitUrgentAppealPage.setKeyboard(submitUrgentAppealKeyboard);

        return mainPage;
    }

}
