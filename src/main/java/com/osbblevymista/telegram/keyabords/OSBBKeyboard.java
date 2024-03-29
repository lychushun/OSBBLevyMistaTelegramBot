package com.osbblevymista.telegram.keyabords;

import com.osbblevymista.telegram.keyabords.buttons.OSBBKeyboardButton;
import com.osbblevymista.telegram.pages.BasePage;
import com.osbblevymista.telegram.system.Actions;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class OSBBKeyboard {

    @Getter
    private KeyboardRow firstKeyboardRow;
    @Getter
    private KeyboardRow secondKeyboardRow;
    @Getter
    private KeyboardRow thirdKeyboardRow;
    @Getter
    private KeyboardRow fourthKeyboardRow;
    @Getter
    private boolean admin;
    @Setter
    protected String sufix = "";

    protected OSBBKeyboardButton osbbKeyboardButtonBack = new OSBBKeyboardButton(Actions.BUTTON_BACK.getText());;
    protected OSBBKeyboardButton osbbKeyboardButtonNext = new OSBBKeyboardButton(Actions.BUTTON_NEXT.getText());;

    private boolean isDisplayBackKey = false;
    private boolean isDisplayNextKey = false;

    public List<String> messages = new ArrayList<>();

    private final List<OSBBKeyboardButton> firstOSBBKeyboardRow = new ArrayList<>();
    private final List<OSBBKeyboardButton> secondOSBBKeyboardRow = new ArrayList<>();
    private final List<OSBBKeyboardButton> thirdOSBBKeyboardRow = new ArrayList<>();
    private final List<OSBBKeyboardButton> fourthOSBBKeyboardRow = new ArrayList<>();

    {
        firstKeyboardRow = new KeyboardRow();
        secondKeyboardRow = new KeyboardRow();
        thirdKeyboardRow = new KeyboardRow();
        fourthKeyboardRow = new KeyboardRow();
    }

    protected OSBBKeyboard(boolean isAdmin) {
        this.admin = isAdmin;
    }

    protected OSBBKeyboard(boolean isAdmin, String sufix) {
        this.sufix = sufix;
        osbbKeyboardButtonBack = new OSBBKeyboardButton(Actions.BUTTON_BACK.getText() + sufix);
        osbbKeyboardButtonNext = new OSBBKeyboardButton(Actions.BUTTON_NEXT.getText() + sufix);
        this.admin = isAdmin;
    }

    public void setPrevPage(BasePage mainPage) {
        if (Objects.nonNull(mainPage)) {
            osbbKeyboardButtonBack.setPrevPage(mainPage);
            osbbKeyboardButtonBack.setId(Actions.BUTTON_BACK.getText()+sufix);
            insertIntoFourthRow(osbbKeyboardButtonBack);

            isDisplayBackKey = true;
        } else {
            isDisplayBackKey = false;
        }
    }

    public void setNextPage(BasePage mainPage) {
        if (Objects.nonNull(mainPage)) {
            osbbKeyboardButtonNext.setNextPage(mainPage);
            osbbKeyboardButtonNext.setId(Actions.BUTTON_NEXT.getText()+sufix);
            insertIntoFourthRow(osbbKeyboardButtonNext);

            isDisplayNextKey = true;
        } else {
            isDisplayNextKey = false;
        }
    }

    public KeyboardButton getBack() {
        return isDisplayBackKey ? osbbKeyboardButtonBack : null;
    }

    public void doExecute() {
    }

    public boolean canExecute() {
        return false;

    }

    public List<KeyboardRow> getKeyBoard() {
        List<KeyboardRow> list = new ArrayList<KeyboardRow>();

        list.add(getFirstKeyboardRow());
        list.add(getSecondKeyboardRow());
        list.add(getThirdKeyboardRow());
        list.add(getFourthKeyboardRow());

        return list;
    }

    protected void insertIntoFirstRow(OSBBKeyboardButton osbbKeyboardButton) {
        if(!osbbKeyboardButton.isOnlyAdmin() || admin) {
            firstOSBBKeyboardRow.add(osbbKeyboardButton);

            List<KeyboardButton> newMap = firstOSBBKeyboardRow.stream().map(keyboardButton -> new KeyboardButton(keyboardButton.getText()))
                    .collect(Collectors.toList());

            firstKeyboardRow = new KeyboardRow(newMap);
        }
    }

    protected void insertIntoSecondRow(OSBBKeyboardButton osbbKeyboardButton) {
        if(!osbbKeyboardButton.isOnlyAdmin() || admin) {
            secondOSBBKeyboardRow.add(osbbKeyboardButton);

            List<KeyboardButton> newMap = secondOSBBKeyboardRow.stream().map(keyboardButton -> new KeyboardButton(keyboardButton.getText()))
                    .collect(Collectors.toList());

            secondKeyboardRow = new KeyboardRow(newMap);
        }
    }

    protected void insertIntoThirdRow(OSBBKeyboardButton osbbKeyboardButton) {
        if(!osbbKeyboardButton.isOnlyAdmin() || admin) {
            thirdOSBBKeyboardRow.add(osbbKeyboardButton);

            List<KeyboardButton> newMap = thirdOSBBKeyboardRow.stream().map(keyboardButton -> new KeyboardButton(keyboardButton.getText()))
                    .collect(Collectors.toList());

            thirdKeyboardRow = new KeyboardRow(newMap);
        }
    }

    protected void insertIntoFourthRow(OSBBKeyboardButton osbbKeyboardButton) {
        if(!osbbKeyboardButton.isOnlyAdmin() || admin) {
            fourthOSBBKeyboardRow.add(osbbKeyboardButton);

            List<KeyboardButton> newMap = fourthOSBBKeyboardRow.stream().map(keyboardButton -> new KeyboardButton(keyboardButton.getText()))
                    .collect(Collectors.toList());

            fourthKeyboardRow = new KeyboardRow(newMap);
        }
    }

    public List<OSBBKeyboardButton> getAllOSBBKeyboardButtons() {
        List<OSBBKeyboardButton> list = Stream.concat(firstOSBBKeyboardRow.stream(), secondOSBBKeyboardRow.stream()).collect(Collectors.toList());
        list = Stream.concat(list.stream(), thirdOSBBKeyboardRow.stream())
                .collect(Collectors.toList());
        return Stream.concat(list.stream(), fourthOSBBKeyboardRow.stream())
                .collect(Collectors.toList());
    }

}
