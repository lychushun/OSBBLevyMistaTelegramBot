package com.osbblevymista.telegram;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.keyabords.*;
import com.osbblevymista.telegram.keyabords.appeals.AppealKeyboard;
import com.osbblevymista.telegram.keyabords.appeals.SubmitSimpleAppealKeyboard;
import com.osbblevymista.telegram.keyabords.appeals.SubmitUrgentAppealKeyboard;
import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.models.UserInfo;
import com.osbblevymista.telegram.pages.*;
import com.osbblevymista.telegram.pages.appeals.AppealPage;
import com.osbblevymista.telegram.pages.appeals.SubmitSimpleAppealPage;
import com.osbblevymista.telegram.pages.appeals.SubmitUrgentAppealPage;
import com.osbblevymista.telegram.send.OSBBSendMessage;
import com.osbblevymista.telegram.send.SendMessageBuilder;
import com.osbblevymista.telegram.send.SendMessageParams;
import com.osbblevymista.telegram.send.processors.ActionSendMessageProcessor;
import com.osbblevymista.telegram.send.processors.AdminProcessor;
import com.osbblevymista.telegram.send.processors.SessionSendMessageProcessor;
import com.osbblevymista.telegram.system.Messages;
import com.osbblevymista.telegram.system.SessionManager;
import com.osbblevymista.telegram.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.PhotoSize;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.LongPollingBot;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
public class TelegramRunner {

    private final SendMessageBuilder sendMessageBuilder;
    private final ActionSendMessageProcessor actionSendMessageProcessor;
    private final SessionSendMessageProcessor sessionSendMessageProcessor;
    private final UserInfoFileReader fileWorker;
    private final AdminProcessor adminProcessor;
    private final SessionManager sessionManager;
    private final AuthUtil authUtil;

    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    @Value("${client.ip}")
    private String clientIp;

    @Value("${client.port}")
    private String clientPort;

    @Bean
    public TaskExecutor taskExecutor() {
        return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
    }

    @Bean
    public CommandLineRunner schedulingRunner(TaskExecutor executor) {
        return new CommandLineRunner() {
            public void run(String... args) throws Exception {
                System.out.println("Running telegram...");
                runTelegram();
            }
        };
    }

    public void runTelegram() throws Exception {
        try {
            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
            LongPollingBot bot = new TelegramLongPollingSessionBot() {

                @Override
                public void onUpdateReceived(Update update, Optional<Session> optional) {

                    try {
                        Message message = update.getMessage();
                        logger.info("REQUEST FROM USER ID: " + message.getFrom().getId() +
                                "; FIRST NAME: " + message.getFrom().getFirstName() +
                                "; LAST NAME: " + message.getFrom().getLastName() +
                                "; USER NAME: " + message.getFrom().getUserName() +
                                "; CHAT ID: " + message.getChatId() +
                                "; MESSAGE: " + message.getText());

                        Long chatId = message.getChatId();

                        List<Function<Message, OSBBSendMessage>> newMessages = new ArrayList<>();

                        SendMessageParams.SendMessageParamsBuilder sendMessageParamsBuilder = getSendMessageParamsBuilder(message, chatId);

                        String strMessage = message.getText();
                        List<PhotoSize> photoSizes = message.getPhoto();

//                        if (strMessage != null || photoSizes != null) {
                        if (strMessage != null) {

                            if (strMessage.equals("/start")) {
                                newMessages = createStartPage(sendMessageParamsBuilder.build(), message.getFrom().getId());
                            } else {
                                UserInfo userInfo = new UserInfo();
                                userInfo.setLastName(message.getChat().getLastName());
                                userInfo.setFirstName(message.getChat().getFirstName());
                                userInfo.setChatId(message.getChat().getId().toString());
                                userInfo.setUserId(message.getFrom().getId());
                                userInfo.setUserName(message.getFrom().getUserName());
                                fileWorker.add(userInfo);

                                //                                sessionManager.addLoginAndPassToSession(optional, message, fileWorker);

//                session.setAttribute(SessionAttributes.LOGIN, "yura.lychushun@gmail.com");
//                session.setAttribute(SessionAttributes.PASS, "31101993");

                                if (update.hasCallbackQuery()) {
                                    Function<CallbackQuery, OSBBSendMessage> newMessage = processCallBack();
                                    execute(newMessage.apply(update.getCallbackQuery()));
                                }

                                if (update.hasMessage()) {
//                                    String login = sessionManager.getSessionLogin(optional);
//                                    String pass = sessionManager.getSessionPass(optional);

                                    String login = authUtil.getSessionLogin(message.getFrom().getId());
                                    String pass = authUtil.getSessionPass(message.getFrom().getId());

                                    sendMessageParamsBuilder
                                            .login(login)
                                            .pass(pass)
                                            .build();

                                    newMessages = sessionSendMessageProcessor.processSession(message, sendMessageParamsBuilder.build(), optional);

                                    if (Objects.isNull(newMessages)) {
                                        OSBBKeyboardButton osbbKeyboardButton = getOSBBKeyboardButton(message);
                                        newMessages = processMessage(sendMessageParamsBuilder.build(), osbbKeyboardButton);
                                    }
                                }
                            }


                        } else {
                            newMessages.add(actionSendMessageProcessor.createSimpleMessage(sendMessageParamsBuilder.build(), Messages.UNRECOGNIZED_COMMAND.getMessage()));
                        }

                        newMessages.forEach(item -> {
                            try {
                                OSBBSendMessage osbbSendMessage = item.apply(update.getMessage());
                                if (osbbSendMessage.getExecutingDelay() > 0) {
                                    Date startDate = new Date();
                                    while (startDate.getTime() + osbbSendMessage.getExecutingDelay() >= new Date().getTime()) {
                                    }
                                }
                                execute(osbbSendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                                logger.error(e.getMessage(), e);
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        logger.error(e.getMessage(), e);
                    }
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
            };

            telegramBotsApi.registerBot(bot);

        } catch (
                TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private List<Function<Message, OSBBSendMessage>> processMessage(SendMessageParams sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {

        List<BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>> biFunctionList = actionSendMessageProcessor.createSendMessageList(osbbKeyboardButton);

        List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
        biFunctionList.forEach(item -> {
            list.add(new Function<Message, OSBBSendMessage>() {
                         @Override
                         public OSBBSendMessage apply(Message message) {
                             SendMessageParams sendMessageParams = SendMessageParams
                                     .builder()
                                     .chatId(message.getChatId())
                                     .login(sendMessageParam.getLogin())
                                     .pass(sendMessageParam.getPass())
                                     .clientIp(clientIp)
                                     .clientPort(clientPort)
                                     .build();

                             return item.apply(sendMessageParams, osbbKeyboardButton);
                         }
                     }
            );
        });

        return list;
    }

    private List<Function<Message, OSBBSendMessage>> createStartPage(SendMessageParams sendMessageParams, long fromId) throws IOException, CsvException, URISyntaxException {
        OSBBKeyboardButton osbbKeyboardButton = new OSBBKeyboardButton();
        osbbKeyboardButton.setNextPage(getPageInfrastructure(isAdmin(fromId)));
        osbbKeyboardButton.messages.add(Messages.START_NEW_PERSON.getMessage());
        return processMessage(sendMessageParams, osbbKeyboardButton);
    }

    private OSBBKeyboardButton getOSBBKeyboardButton(Message message) throws IOException, CsvException {
        String buttonText = message.getText();
        MainPage mainPage = getPageInfrastructure(isAdmin(message.getFrom().getId()));
        return mainPage.currentButton(buttonText);
    }

    public Function<CallbackQuery, OSBBSendMessage> processCallBack() {
        return new Function<CallbackQuery, OSBBSendMessage>() {
            @Override
            public OSBBSendMessage apply(CallbackQuery callbackQuery) {
                Message sendMessage = callbackQuery.getMessage();
                OSBBSendMessage newMessage = sendMessageBuilder.createEmptyMessage(sendMessage.getChatId());
                newMessage.setText(callbackQuery.getData());
                return newMessage;
            }

        };
    }

    private boolean isAdmin(long adminId) throws IOException, CsvException {
//        AdminProcessor adminProcessor = AdminProcessor.createInstance();
//        adminProcessor.addAdmin("Yura", "", 759291097);
        return adminProcessor.isAdmin(adminId);
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

    public MainPage getPageInfrastructure(boolean isAdmin) {
        MainPage mainPage = MainPage.getInstance(isAdmin);
        ContactPage contactPage = ContactPage.getInstance(isAdmin);
        ArrearsPage arrearsPage = ArrearsPage.getInstance(isAdmin);
        SettingsPage settingsPage = SettingsPage.getInstance(isAdmin);
        AppealPage appealPage = AppealPage.getInstance(isAdmin);
        SubmitSimpleAppealPage submitSimpleAppealPage = SubmitSimpleAppealPage.getInstance(isAdmin);
        SubmitUrgentAppealPage submitUrgentAppealPage = SubmitUrgentAppealPage.getInstance(isAdmin);
        AdminPage adminPage = AdminPage.getInstance(isAdmin);
        ChatPage chatPage = ChatPage.getInstance(isAdmin);
        InfoPage infoPage = InfoPage.getInstance(isAdmin);
        ReportPage reportPage = ReportPage.getInstance(isAdmin);

        MainKeyboard mainKeyboard = new MainKeyboard(isAdmin);
        mainKeyboard.setArrearsPage(arrearsPage);
        mainKeyboard.setInfoPage(infoPage);
        mainKeyboard.setSettingsPage(settingsPage);
        mainKeyboard.setAppealPage(appealPage);
        mainKeyboard.setAdminPage(adminPage);
        mainKeyboard.setChatPage(chatPage);

        ContactKeyboard contactKeyboard = new ContactKeyboard(isAdmin);
        contactKeyboard.setPrevPage(infoPage);

        SettingsKeyboard settingsKeyboard = new SettingsKeyboard(isAdmin);
        settingsKeyboard.setPrevPage(mainPage);

        AppealKeyboard appealKeyboard = new AppealKeyboard(isAdmin);
        appealKeyboard.setPrevPage(mainPage);
        appealKeyboard.setSubmitSimplePage(submitSimpleAppealPage);
        appealKeyboard.setSubmitUrgentPage(submitUrgentAppealPage);

        AdminKeyboard adminKeyboard = new AdminKeyboard(isAdmin);
        adminKeyboard.setPrevPage(mainPage);

        InfoKeyboard infoKeyboard = new InfoKeyboard(isAdmin);
        infoKeyboard.setPrevPage(mainPage);
        infoKeyboard.setContactPage(contactPage);
        infoKeyboard.setReportPage(reportPage);

        SubmitSimpleAppealKeyboard submitSimpleAppealKeyboard = new SubmitSimpleAppealKeyboard(isAdmin);
        submitSimpleAppealKeyboard.setPrevPage(appealPage);

        SubmitUrgentAppealKeyboard submitUrgentAppealKeyboard = new SubmitUrgentAppealKeyboard(isAdmin);
        submitUrgentAppealKeyboard.setPrevPage(appealPage);

        mainPage.setKeyboard(mainKeyboard);
        contactPage.setKeyboard(contactKeyboard);
        settingsPage.setKeyboard(settingsKeyboard);
        appealPage.setKeyboard(appealKeyboard);
        adminPage.setKeyboard(adminKeyboard);
        infoPage.setKeyboard(infoKeyboard);
        submitSimpleAppealPage.setKeyboard(submitSimpleAppealKeyboard);
        submitUrgentAppealPage.setKeyboard(submitUrgentAppealKeyboard);

        return mainPage;
    }

}
