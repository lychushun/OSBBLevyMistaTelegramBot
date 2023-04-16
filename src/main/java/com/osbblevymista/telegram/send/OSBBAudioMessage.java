package com.osbblevymista.telegram.send;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendDocument;
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto;
import org.telegram.telegrambots.meta.api.objects.Audio;

@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class OSBBAudioMessage extends SendAudio implements OSBBSendMessage{

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
