package com.osbblevymista.telegram.view.keyabords;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class KeyboardParam {

    private String chatId;
    private String miyDimCookie;
    private String clientIp;
    private String clientPort;

}
