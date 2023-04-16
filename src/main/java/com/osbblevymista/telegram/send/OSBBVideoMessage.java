package com.osbblevymista.telegram.send;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendVideo;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OSBBVideoMessage extends SendVideo implements OSBBSendMessage{

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
