package com.osbblevymista.pages;

import com.osbblevymista.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.keyabords.buttons.OSBBInlineKeyboardButton;
import com.osbblevymista.system.Actions;
import com.osbblevymista.system.Links;
import com.osbblevymista.system.Messages;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.function.Function;

public class ChatPage extends BasePage {

    private static ChatPage chatPage;

    public static ChatPage getInstance(boolean isAdmin) {
        if (chatPage == null) {
            chatPage = new ChatPage();
        }
        chatPage.setAdmin(isAdmin);

        return chatPage;
    }

    @Override
    public void doExecute() {
    }

    @Override
    public boolean doExecute(PageParams sendMessageParams, Function<ExecutorListenerResponse, Boolean> consumer) throws UnsupportedEncodingException, URISyntaxException {

        ExecutorListenerResponse executorListenerResponse = new ExecutorListenerResponse();
        executorListenerResponse.messages.add(Messages.CHATS_BASE.getMessage());

        OSBBInlineKeyboardButton osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_CHAT_OSBB_CHAT_TEREVENI.getText(), Links.OSBB_TELEGRAM_CHAT_TEREVENI.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_CHAT_OSBB_CHAT_TEREVENI.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        osbbInlineKeyboardButton =
                new OSBBInlineKeyboardButton(Actions.BUTTON_CHAT_OSBB_CHAT_ANNOUNCEMENT.getText(), Links.OSBB_TELEGRAM_CHAT_TEREVENI.getLink());
        osbbInlineKeyboardButton.setId(Actions.BUTTON_CHAT_OSBB_CHAT_TEREVENI.getText());
        executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);

        if (isAdmin()) {
            executorListenerResponse.messages.add(Messages.CHATS_BOARD.getMessage());
            osbbInlineKeyboardButton =
                    new OSBBInlineKeyboardButton(Actions.BUTTON_CHAT_OSBB_CHAT_BOARD.getText(), Links.OSBB_TELEGRAM_CHAT_BOARD.getLink());
            osbbInlineKeyboardButton.setId(Actions.BUTTON_CHAT_OSBB_CHAT_BOARD.getText());
            executorListenerResponse.insertOSBBInlineKeyboardButtonNextRow(osbbInlineKeyboardButton);
        }

        return consumer.apply(executorListenerResponse);
    }

    @Override
    public boolean canExecute() {
        return true;
    }

    private ChatPage() {
        super();
        title = "Чати";
    }
}
