package com.osbblevymista.telegram.send;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.function.Function;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OSBBSendMessage extends SendMessage {

    public Function<String, String> sendMessage;

    @Value("${defaultExecutionDelay}")
    private int executingDelay;

}
