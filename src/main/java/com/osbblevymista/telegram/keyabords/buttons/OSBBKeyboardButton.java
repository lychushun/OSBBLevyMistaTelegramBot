package com.osbblevymista.telegram.keyabords.buttons;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.osbblevymista.telegram.executorlistener.ExecutorListenerResponse;
import com.osbblevymista.telegram.executorlistener.OSBBExecutorListener;
import com.osbblevymista.telegram.keyabords.KeyboardParam;
import com.osbblevymista.telegram.pages.BasePage;
import lombok.Getter;
import lombok.Setter;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Setter
@Getter
public class OSBBKeyboardButton extends KeyboardButton {

    @JsonProperty("resize_keyboard")
    private Boolean resizeKeyboard = true;

    private BasePage nextPage;
    private BasePage prevPage;
    private String id;

    private boolean onlyAdmin = false;

    private OSBBExecutorListener osbbExecutorListener;

    public List<String> messages = new ArrayList<>();

    public OSBBKeyboardButton(){}

    public OSBBKeyboardButton(String text){
        super(text);
    }

    public boolean doExecute(KeyboardParam keyboardParam, Function<ExecutorListenerResponse, Boolean> consumer) throws IOException, URISyntaxException {
        ExecutorListenerResponse executorListenerResponse = osbbExecutorListener.doExecute(keyboardParam);
        return consumer.apply(executorListenerResponse);
    }

    public boolean canExecute(){
        return Objects.nonNull(osbbExecutorListener);
    }

}
