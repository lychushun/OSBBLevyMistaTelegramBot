//package com.osbblevymista;
//
//import com.opencsv.exceptions.CsvException;
//import com.osbblevymista.filereaders.UserInfoFileReader;
//import com.osbblevymista.keyabords.*;
//import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
//import com.osbblevymista.models.UserInfo;
//import com.osbblevymista.pages.*;
//import com.osbblevymista.send.OSBBSendMessage;
//import com.osbblevymista.send.SendMessageBuilder;
//import com.osbblevymista.send.SendMessageParams;
//import com.osbblevymista.send.processors.ActionSendMessageProcessor;
//import com.osbblevymista.send.processors.AdminProcessor;
//import com.osbblevymista.send.processors.SessionSendMessageProcessor;
//import com.osbblevymista.system.SessionAttributes;
//import lombok.AllArgsConstructor;
//import lombok.NoArgsConstructor;
//import lombok.RequiredArgsConstructor;
//import org.apache.commons.lang3.ObjectUtils;
//import org.apache.shiro.session.Session;
//import org.checkerframework.checker.units.qual.A;
//import org.checkerframework.checker.units.qual.C;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.context.ApplicationContext;
//import org.springframework.stereotype.Component;
//import org.telegram.telegrambots.meta.TelegramBotsApi;
//import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
//import org.telegram.telegrambots.meta.api.objects.Message;
//import org.telegram.telegrambots.meta.api.objects.Update;
//import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
//import org.telegram.telegrambots.meta.generics.LongPollingBot;
//import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;
//import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
//
//import java.io.IOException;
//import java.io.UnsupportedEncodingException;
//import java.net.URISyntaxException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.function.BiFunction;
//import java.util.function.Function;
//
//@Component
//@RequiredArgsConstructor
//public class TelegramStarter1 implements CommandLineRunner {
//
//    private final SendMessageBuilder sendMessageBuilder;
//    private final ActionSendMessageProcessor actionSendMessageProcessor;
//    private final SessionSendMessageProcessor sessionSendMessageProcessor;
//
//    //    public void run(String[] args) {
//
//    @Override
//    public void run(String... args) throws Exception {
//
//        System.out.println("hello");
//
//        try {
//            TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
//            LongPollingBot bot = new TelegramLongPollingSessionBot() {
//
//                @Override
//                public void onUpdateReceived(Update update, Optional<Session> optional) {
//
//                    try {
//                        Session session = optional.get();
//                        Message message = update.getMessage();
//                        Long chatId = message.getChatId();
//
//                        UserInfo userInfo = createUserInfo(
//                                chatId.toString(),
//                                message.getChat().getFirstName(),
//                                message.getChat().getLastName(),
//                                message.getFrom().getId()
//                        );
//
//                        if (isAddedChatId(userInfo) && Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED))) {
//                            session.setAttribute(SessionAttributes.IS_ADDED, true);
//                        } else if (Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED)) || !isAddedChatId(userInfo)) {
//                            Boolean isAdded = addChatId(userInfo);
//                            session.setAttribute(SessionAttributes.IS_ADDED, true);
//                        }
//
//                        session.setAttribute(SessionAttributes.LOGIN, "yura.lychushun@gmail.com");
//                        session.setAttribute(SessionAttributes.PASS, "31101993");
//
//
//                        if (update.hasCallbackQuery()) {
//                            Function<CallbackQuery, OSBBSendMessage> newMessage = processCallBack();
//                            try {
//                                execute(newMessage.apply(update.getCallbackQuery()));
//                            } catch (TelegramApiException e) {
//                                e.printStackTrace();
//                            }
//                        }
//
//                        if (update.hasMessage()) {
//                            String strMessage = message.getText();
//
//                            List<Function<Message, OSBBSendMessage>> newMessages = new ArrayList<>();
//
//                            if (strMessage.equals("/start")) {
//                                OSBBKeyboardButton osbbKeyboardButton = new OSBBKeyboardButton();
//                                osbbKeyboardButton.setNextPage(getPageInfrastructure(isAdmin(message.getFrom().getId())));
//
//                                SendMessageParams sendMessageParams = SendMessageParams.builder()
//                                        .chatId(chatId)
//                                        .build();
//                                newMessages = processMessage(sendMessageParams, osbbKeyboardButton);
//                            } else {
//
//                                String login = (String) session.getAttribute(SessionAttributes.LOGIN);
//                                String pass = (String) session.getAttribute(SessionAttributes.PASS);
//
//                                SendMessageParams sendMessageParams = SendMessageParams
//                                        .builder()
//                                        .login(login)
//                                        .pass(pass)
//                                        .chatId(chatId)
//                                        .build();
//
////                                SessionSendMessageProcessor sessionProcessor = new SessionSendMessageProcessor();
//                                newMessages = sessionSendMessageProcessor.processSession(strMessage, sendMessageParams, session);
//
//                                if (Objects.isNull(newMessages)) {
//                                    OSBBKeyboardButton osbbKeyboardButton = getOSBBKeyboardButton(message);
//                                    newMessages = processMessage(sendMessageParams, osbbKeyboardButton);
//                                }
//                            }
//
//                            newMessages.forEach(item -> {
//                                try {
//                                    execute(item.apply(update.getMessage()));
//                                } catch (TelegramApiException e) {
//                                    e.printStackTrace();
//                                }
//                            });
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//
//
//                @Override
//                public void onUpdatesReceived(List<Update> updates) {
//                    super.onUpdatesReceived(updates);
//                }
//
//                @Override
//                public String getBotUsername() {
//                    return "OSBBLevyMista";
//                }
//
//                @Override
//                public String getBotToken() {
//                    return "5574766925:AAGt6eB9v2cZ-ZWRxtUK_NpXN4siZpCpbMw";
//                }
//
//                @Override
//                public void onRegister() {
//                    super.onRegister();
//                }
//            };
//            telegramBotsApi.registerBot(bot);
//
//        } catch (
//                TelegramApiException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static UserInfo createUserInfo(String chatId, String firstName, String lastName, long userId) {
//        UserInfo userInfo = new UserInfo();
//
//        userInfo.setLastName(ObjectUtils.isEmpty(lastName) ? "-" : lastName);
//        userInfo.setFirstName(firstName);
//        userInfo.setChatId(chatId);
//        userInfo.setUserId(userId);
//
//        return userInfo;
//    }
//
//    private static boolean isAddedChatId(UserInfo userInfo) throws IOException, CsvException {
//        UserInfoFileReader fileWorker = UserInfoFileReader.createInstance();
//        return fileWorker.isAddedUserInfo(userInfo);
//    }
//
//
//    private boolean addChatId(UserInfo userInfo) throws IOException, CsvException {
//        UserInfoFileReader fileWorker = UserInfoFileReader.createInstance();
//        return fileWorker.add(userInfo);
//    }
//
//    public List<Function<Message, OSBBSendMessage>> processMessage(SendMessageParams
//                                                                           sendMessageParam, OSBBKeyboardButton osbbKeyboardButton) throws UnsupportedEncodingException, URISyntaxException {
//
//        List<BiFunction<SendMessageParams, OSBBKeyboardButton, OSBBSendMessage>> biFunctionList = actionSendMessageProcessor.createSendMessageList(osbbKeyboardButton);
//
//        List<Function<Message, OSBBSendMessage>> list = new ArrayList<>();
//        biFunctionList.forEach(item -> {
//            list.add(new Function<Message, OSBBSendMessage>() {
//                         @Override
//                         public OSBBSendMessage apply(Message message) {
//                             SendMessageParams sendMessageParams = SendMessageParams
//                                     .builder()
//                                     .chatId(message.getChatId())
//                                     .login(sendMessageParam.getLogin())
//                                     .pass(sendMessageParam.getPass())
//                                     .build();
//
//                             return item.apply(sendMessageParams, osbbKeyboardButton);
//                         }
//                     }
//            );
//        });
//
//        return list;
//    }
////    759291097
//
//    private OSBBKeyboardButton getOSBBKeyboardButton(Message message) throws IOException, CsvException {
//        String buttonText = message.getText();
//        MainPage mainPage = getPageInfrastructure(isAdmin(message.getFrom().getId()));
//
//        return mainPage.currentButton(buttonText);
//    }
//
//    public Function<CallbackQuery, OSBBSendMessage> processCallBack() {
//        return new Function<CallbackQuery, OSBBSendMessage>() {
//            @Override
//            public OSBBSendMessage apply(CallbackQuery callbackQuery) {
//                Message sendMessage = callbackQuery.getMessage();
//                OSBBSendMessage newMessage = sendMessageBuilder.createEmptyMessage(sendMessage.getChatId());
//                newMessage.setText(callbackQuery.getData());
//                return newMessage;
//            }
//
//        };
//    }
//
//    private boolean isAdmin(long adminId) throws IOException, CsvException {
//        AdminProcessor adminProcessor = AdminProcessor.createInstance();
////        adminProcessor.addAdmin("Yura", "", 759291097);
//        return adminProcessor.isAdmin(adminId);
//    }
//
//    public MainPage getPageInfrastructure(boolean isAdmin) {
//        MainPage mainPage = MainPage.getInstance(isAdmin);
//        ContactPage contactPage = ContactPage.getInstance(isAdmin);
//        ArrearsPage arrearsPage = ArrearsPage.getInstance(isAdmin);
//        SettingsPage settingsPage = SettingsPage.getInstance(isAdmin);
//        AppealPage appealPage = AppealPage.getInstance(isAdmin);
//        AdminPage adminPage = AdminPage.getInstance(isAdmin);
//        ChatPage chatPage = ChatPage.getInstance(isAdmin);
//        InfoPage infoPage = InfoPage.getInstance(isAdmin);
//
//        MainKeyboard mainKeyboard = new MainKeyboard(isAdmin);
//        mainKeyboard.setArrearsPage(arrearsPage);
//        mainKeyboard.setInfoPage(infoPage);
//        mainKeyboard.setSettingsPage(settingsPage);
//        mainKeyboard.setAppealPage(appealPage);
//        mainKeyboard.setAdminPage(adminPage);
//        mainKeyboard.setChatPage(chatPage);
//
//        ContactKeyboard contactKeyboard = new ContactKeyboard(isAdmin);
//        contactKeyboard.setPrevPage(infoPage);
//
//        SettingsKeyboard settingsKeyboard = new SettingsKeyboard(isAdmin);
//        settingsKeyboard.setPrevPage(mainPage);
//
//        AppealKeyboard appealKeyboard = new AppealKeyboard(isAdmin);
//        appealKeyboard.setPrevPage(mainPage);
//
//        AdminKeyboard adminKeyboard = new AdminKeyboard(isAdmin);
//        adminKeyboard.setPrevPage(mainPage);
//
//        InfoKeyboard infoKeyboard = new InfoKeyboard(isAdmin);
//        infoKeyboard.setPrevPage(mainPage);
//        infoKeyboard.setContactPage(contactPage);
//
//        mainPage.setKeyboard(mainKeyboard);
//        contactPage.setKeyboard(contactKeyboard);
//        settingsPage.setKeyboard(settingsKeyboard);
//        appealPage.setKeyboard(appealKeyboard);
//        adminPage.setKeyboard(adminKeyboard);
//        infoPage.setKeyboard(infoKeyboard);
//
//        return mainPage;
//    }
//
//}
