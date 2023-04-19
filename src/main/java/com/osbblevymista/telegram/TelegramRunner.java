//package com.osbblevymista.telegram;
//
//import com.opencsv.exceptions.CsvException;
//import com.osbblevymista.OSBBLevyMistaTelegramLongPollingSessionBot;
//import com.osbblevymista.miydim.services.MiyDimService;
//import com.osbblevymista.telegram.view.keyabords.*;
//import com.osbblevymista.telegram.view.keyabords.appeals.AppealKeyboard;
//import com.osbblevymista.telegram.view.keyabords.appeals.SubmitSimpleAppealKeyboard;
//import com.osbblevymista.telegram.view.keyabords.appeals.SubmitUrgentAppealKeyboard;
//import com.osbblevymista.telegram.view.keyabords.buttons.OSBBKeyboardButton;
//import com.osbblevymista.telegram.models.UserInfo;
//import com.osbblevymista.telegram.view.pages.*;
//import com.osbblevymista.telegram.view.pages.appeals.AppealPage;
//import com.osbblevymista.telegram.view.pages.appeals.SubmitSimpleAppealPage;
//import com.osbblevymista.telegram.view.pages.appeals.SubmitUrgentAppealPage;
//import com.osbblevymista.telegram.send.OSBBSendMessage;
//import com.osbblevymista.telegram.send.messages.SendMessageBuilder;
//import com.osbblevymista.telegram.send.messages.SendMessageParams;
//import com.osbblevymista.telegram.send.processors.ActionSendMessageProcessor;
//import com.osbblevymista.telegram.send.processors.SessionSendMessageProcessor;
//import com.osbblevymista.telegram.services.AdminInfoService;
//import com.osbblevymista.telegram.services.UserInfoService;
//import com.osbblevymista.telegram.system.Messages;
//import lombok.RequiredArgsConstructor;
//import org.apache.shiro.session.Session;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.task.SimpleAsyncTaskExecutor;
//import org.springframework.core.task.TaskExecutor;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.PhotoSize;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.generics.LongPollingBot;
//import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.util.*;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
//import static java.util.Objects.isNull;
//
//@Configuration
//@RequiredArgsConstructor
//public class TelegramRunner {
//
//    private final OSBBLevyMistaTelegramLongPollingSessionBot osbbLevyMistaTelegramLongPollingSessionBot;
//
////    private final SendMessageBuilder sendMessageBuilder;
////    private final ActionSendMessageProcessor actionSendMessageProcessor;
////    private final SessionSendMessageProcessor sessionSendMessageProcessor;
////    private final UserInfoService userInfoService;
////    private final AdminInfoService adminInfoService;
////    private final MiyDimService miyDimService;
////
////
////    private final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);
////
////    @Value("${telegram.bot.token}")
////    private String token;
////
////    @Value("${telegram.bot.name}")
////    private String name;
////
////    @Value("${client.ip}")
////    private String clientIp;
////
////    @Value("${client.port}")
////    private String clientPort;
//
//    @Bean(name = "telegramTaskExecutor")
//    public TaskExecutor taskExecutor() {
//        return new SimpleAsyncTaskExecutor(); // Or use another one of your liking
//    }
//
//    @Bean
//    public CommandLineRunner schedulingTelegramRunner(@Qualifier("telegramTaskExecutor") TaskExecutor executor) {
//        return new CommandLineRunner() {
//            public void run(String... args) throws Exception {
//                System.out.println("Running telegram...");
//                runTelegram();
//            }
//        };
//    }
//
//    public void runTelegram() throws Exception {
//        try {
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
////            LongPollingBot bot = new TelegramLongPollingSessionBot() {
////
////                @Override
////                public void onUpdateReceived(Update update, Optional<Session> optional) {
////
////                    try {
////                        Message message = update.getMessage();
////                        if (message != null) {
////                            logger.info("REQUEST FROM USER ID: " + message.getFrom().getId() +
////                                    "; FIRST NAME: " + message.getFrom().getFirstName() +
////                                    "; LAST NAME: " + message.getFrom().getLastName() +
////                                    "; USER NAME: " + message.getFrom().getUserName() +
////                                    "; CHAT ID: " + message.getChatId() +
////                                    "; MESSAGE: " + message.getText());
////
////                            Long chatId = message.getChatId();
////
////                            List<Function<Message, OSBBSendMessage>> newMessages = new ArrayList<>();
////                            List<Function<Message, List<OSBBSendMessage>>> newMultiMessages = new ArrayList<>();
////
////                            SendMessageParams.SendMessageParamsBuilder sendMessageParamsBuilder = getSendMessageParamsBuilder(message, chatId);
////
////                            String strMessage = message.getText();
////                            List<PhotoSize> photoSizes = message.getPhoto();
////
//////                        if (strMessage != null || photoSizes != null) {
////                            if (strMessage != null) {
////
////                                if (strMessage.equals("/start")) {
////                                    newMessages = createStartPage(sendMessageParamsBuilder.build(), message.getFrom().getId());
////                                } else {
////                                    UserInfo userInfo = new UserInfo();
////                                    userInfo.setLastName(message.getChat().getLastName());
////                                    userInfo.setFirstName(message.getChat().getFirstName());
////                                    userInfo.setChatId(message.getChat().getId().toString());
////                                    userInfo.setUserId(message.getFrom().getId());
////                                    userInfo.setUserName(message.getFrom().getUserName());
////                                    userInfoService.addRow(userInfo);
//////                                fileWorker.add(userInfo);
////
////                                    //                                sessionManager.addLoginAndPassToSession(optional, message, fileWorker);
////
//////                session.setAttribute(SessionAttributes.LOGIN, "yura.lychushun@gmail.com");
//////                session.setAttribute(SessionAttributes.PASS, "31101993");
////
////                                    if (update.hasCallbackQuery()) {
////                                        Function<CallbackQuery, OSBBSendMessage> newMessage = processCallBack();
////                                        execute(newMessage.apply(update.getCallbackQuery()));
////                                    }
////
////                                    if (update.hasMessage()) {
////                                        sendMessageParamsBuilder
////                                                .clientPort(clientPort)
////                                                .clientIp(clientIp)
////                                                .chatId(chatId)
////                                                .build();
////
////                                        newMultiMessages = sessionSendMessageProcessor.processSession(message, sendMessageParamsBuilder.build(), optional);
////
////                                        if (isNull(newMultiMessages)) {
////                                            OSBBKeyboardButton osbbKeyboardButton = getOSBBKeyboardButton(message);
////                                            newMessages = processMessage(sendMessageParamsBuilder.build(), osbbKeyboardButton);
////                                        }
////                                    }
////                                }
////
////                            } else {
////                                newMessages.add(actionSendMessageProcessor.createSimpleMessage(sendMessageParamsBuilder.build(), Messages.UNRECOGNIZED_COMMAND.getMessage()));
////                            }
////
////                            newMessages.forEach(item -> {
////                                try {
////                                    OSBBSendMessage osbbSendMessage = item.apply(update.getMessage());
////                                    if (osbbSendMessage.getExecutingDelay() > 0) {
////                                        Date startDate = new Date();
////                                        while (startDate.getTime() + osbbSendMessage.getExecutingDelay() >= new Date().getTime()) {
////                                        }
////                                    }
////                                    execute(osbbSendMessage);
////                                } catch (TelegramApiException e) {
////                                    e.printStackTrace();
////                                    logger.error(e.getMessage(), e);
////                                }
////                            });
////                        }
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                        logger.error(e.getMessage(), e);
////                    }
////                }
////
////                @Override
////                public void onUpdatesReceived(List<Update> updates) {
////                    super.onUpdatesReceived(updates);
////                }
////
////                @Override
////                public String getBotUsername() {
////                    return name;
////                }
////
////                @Override
////                public String getBotToken() {
////                    return token;
////                }
////
////                @Override
////                public void onRegister() {
////                    super.onRegister();
////                }
////
////                private void executeMultiMessages(List<Function<Message, List<OSBBSendMessage>>> miltyMessages, Update update) {
////                    miltyMessages.forEach(item -> {
////                        try {
////                            List<OSBBSendMessage> osbbSendMessage = item.apply(update.getMessage());
////                            osbbSendMessage.forEach(el -> {
////
////                                if (osbbSendMessage.getExecutingDelay() > 0) {
////                                    Date startDate = new Date();
////                                    while (startDate.getTime() + osbbSendMessage.getExecutingDelay() >= new Date().getTime()) {
////                                    }
////                                }
////                                execute(osbbSendMessage);
////                            });
////                        } catch (TelegramApiException e) {
////                            e.printStackTrace();
////                            logger.error(e.getMessage(), e);
////                        }
////                    });
////                }
////            };
//
//            telegramBotsApi.registerBot(osbbLevyMistaTelegramLongPollingSessionBot);
//
//        } catch (
//                TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//
//
//
//
//
////    private boolean isAdmin(long adminId) throws IOException, CsvException {
//////        AdminProcessor adminProcessor = AdminProcessor.createInstance();
//////        adminProcessor.addAdmin("Yura", "", 759291097);
////        return adminInfoService.isAdmin(adminId);
////    }
//
//
//
//}
