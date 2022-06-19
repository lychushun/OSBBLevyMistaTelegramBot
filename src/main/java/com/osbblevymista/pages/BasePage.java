package com.osbblevymista.pages;

import com.osbblevymista.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.keyabords.OSBBKeyboard;
import com.osbblevymista.keyabords.buttons.OSBBKeyboardButton;
import lombok.Getter;
import lombok.Setter;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;


@Getter
public abstract class BasePage {

    protected OSBBKeyboard osbbKeyboard;

    public List<String> messages = new ArrayList<>();

    protected String pageId;
    protected String title;

    @Setter
    private boolean admin;

    protected BasePage(){
    }

    public void doExecute() {
    }

    public boolean doExecute(PageParams sendMessageParams, Function<ExecutorListenerResponse, Boolean> consumer) throws UnsupportedEncodingException, URISyntaxException {
        return false;
    }

    public boolean canExecute() {
        return false;
    }

    public boolean isOSBBKeyboard() {
        return Objects.nonNull(osbbKeyboard);
    }

    public void setKeyboard(OSBBKeyboard osbbKeyboard) {
        this.osbbKeyboard = osbbKeyboard;
    }

//    public BasePage next(String buttonId){
//        List<OSBBKeyboardButton> osbbKeyboardButtonList = osbbKeyboard.getAllOSBBKeyboardButtons();
//
//        for (OSBBKeyboardButton item: osbbKeyboardButtonList){
//            if (Objects.equals(item.getId(), buttonId)){
//                if (Objects.nonNull(item.getNextPage())) {
//                    return item.getNextPage();
//                } else {
//                    return item.getPrevPage();
//                }
//            } else {
//                if (Objects.nonNull(item.getNextPage())) {
//                    BasePage basePage = item.getNextPage().next(buttonId);
//                    if (Objects.nonNull(basePage)) {
//                        return basePage;
//                    }
//                }
//            }
//        }
//
//        return null;
//    }

    public OSBBKeyboardButton currentButton(String buttonId) {
//        if (Objects.isNull(osbbKeyboard)) {
//            throw new NullPointerException("OSBB Inline KeyboardButton id cannot be null");
//        }

        if (Objects.nonNull(osbbKeyboard)) {
            List<OSBBKeyboardButton> osbbKeyboardButtonList = osbbKeyboard.getAllOSBBKeyboardButtons();

            for (OSBBKeyboardButton item : osbbKeyboardButtonList) {
                if (Objects.equals(item.getId(), buttonId)) {
                    return item;
                } else {
                    if (Objects.nonNull(item.getNextPage())) {
                        OSBBKeyboardButton osbbKeyboardButton = item.getNextPage().currentButton(buttonId);
                        if (Objects.nonNull(osbbKeyboardButton)) {
                            return osbbKeyboardButton;
                        }
                    }
                }
            }
        }

        return null;
    }

}
