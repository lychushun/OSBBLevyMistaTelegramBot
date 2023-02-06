package com.osbblevymista.telegram.keyabords;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeyboardParam {

    String login;
    String pass;
    String chatId;

}
