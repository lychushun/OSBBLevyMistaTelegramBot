package com.osbblevymista.telegram.botexecution;

import com.osbblevymista.telegram.send.messages.OSBBSendMessage;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

public class BotExecution {

    private final List<BotExecutionObject> botExecutionObjects;

    {
        botExecutionObjects = new ArrayList<>();
    }

    public boolean isEmpty() {
        return botExecutionObjects.size() == 0;
    }

    public void addExecutionsForMessage(BotExecutionObject botExecutionObject) {
        botExecutionObjects.add(botExecutionObject);
    }

    public void addExecutionsForMessage(List<BotExecutionObject> botExecutionObject) {
        botExecutionObjects.addAll(botExecutionObject);
    }

    public void execute(Message message, Consumer<PartialBotApiMethod<Message>> closure) {
        botExecutionObjects.forEach(executionObject -> {

            List<PartialBotApiMethod<Message>> list = executionObject.execute(message);

            list.forEach(it -> {

                OSBBSendMessage osbbSendMessage = (OSBBSendMessage)it;

                if (osbbSendMessage.getExecutingDelay() > 0) {
                    Date startDate = new Date();
                    while (startDate.getTime() + osbbSendMessage.getExecutingDelay() >= new Date().getTime()) {
                    }
                }
                closure.accept(it);
            });

        });
    }

    public static BotExecution empty() {
        return new BotExecution();
    }

}
