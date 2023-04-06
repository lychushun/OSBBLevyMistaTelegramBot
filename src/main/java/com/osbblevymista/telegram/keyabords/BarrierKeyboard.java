//package com.osbblevymista.telegram.keyabords;
//
//import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
//import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
//import com.osbblevymista.telegram.keyabords.buttons.OSBBInlineKeyboardButton;
//import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
//import com.osbblevymista.telegram.pages.ContactPage1;
//import com.osbblevymista.telegram.pages.ReportPage;
//import com.osbblevymista.telegram.system.Actions;
//import com.osbblevymista.telegram.system.InvisibleCharacters;
//import com.osbblevymista.telegram.system.Links;
//import com.osbblevymista.telegram.system.Messages;
//import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
//
//import java.io.IOException;
//import java.net.URISyntaxException;
//
//public class BarrierKeyboard extends OSBBKeyboard {
//    public BarrierKeyboard(boolean isAdmin) {
//        super(isAdmin, InvisibleCharacters._200A.getVal());
//    }
//
//    private final OSBBKeyboardButton osbbKeyboardButtonBarrier1 = new OSBBKeyboardButton(Actions.BUTTON_BARRIER1.getText());
//    private final OSBBKeyboardButton osbbKeyboardButtonBarrier2 = new OSBBKeyboardButton(Actions.BUTTON_BARRIER2.getText());
//    private final OSBBKeyboardButton osbbKeyboardButtonBarrier3 = new OSBBKeyboardButton(Actions.BUTTON_BARRIER3.getText());
//
//    {
//        osbbKeyboardButtonBarrier1.setId(Actions.BUTTON_BARRIER1.getText());
//        osbbKeyboardButtonBarrier1.messages.add(Messages.BARRIER1.getMessage());
//        osbbKeyboardButtonBarrier1.requestContact(true);
//        insertIntoFirstRow(osbbKeyboardButtonBarrier1);
//
//        osbbKeyboardButtonBarrier2.setId(Actions.BUTTON_BARRIER2.getText());
//        osbbKeyboardButtonBarrier2.messages.add(Messages.BARRIER2.getMessage());
//        insertIntoFirstRow(osbbKeyboardButtonBarrier2);
//
//        osbbKeyboardButtonBarrier3.setId(Actions.BUTTON_BARRIER3.getText());
//        osbbKeyboardButtonBarrier2.messages.add(Messages.BARRIER2.getMessage());
//        insertIntoFirstRow(osbbKeyboardButtonBarrier3);
//
//    }
//
//    @Override
//    public KeyboardButton getBack() {
//        return null;
//    }
//}
