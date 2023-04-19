package com.osbblevymista.telegram.send.messages;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OSBBStrMessage extends SendMessage implements OSBBSendMessage {

    public Function<String, String> sendMessage;

//    @Value("${defaultExecutionDelay}")
    private Integer executingDelay = 200;

    @Override
    public Integer getExecutingDelay() {
        return executingDelay;
    }

    @Override
    public void setExecutingDelay(Integer executingDelay) {
        this.executingDelay = executingDelay;
    }
}
