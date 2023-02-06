package com.osbblevymista.telegram.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Function;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSBBSendMessage extends SendMessage {

    public Function<String, String> sendMessage;
    private int executingDelay = 0;

}
