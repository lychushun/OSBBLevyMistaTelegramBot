package com.osbblevymista.telegram.view.pages;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PageParams {

    private String cookie;
    private String clientIp;
    private String clientPort;
    private Long chatId;

}
