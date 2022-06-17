package com.osbblevymista.messages;

import java.util.HashMap;

public abstract class OSBBMessage {

    protected HashMap<String, String> messages;
    public abstract String getMessage(String type);
}
