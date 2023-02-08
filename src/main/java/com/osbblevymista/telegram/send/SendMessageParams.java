package com.osbblevymista.telegram.send;


import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class SendMessageParams {

    private Long chatId;
    private long userId;

    private String loginCookie;

    private String clientIp;
    private String clientPort;

    private String firstName = "";
    private String lastName = "";
    private String userName = "";

    private int executingDelay;

    public String getUserName(){
        return "@"+userName;
    }

    public String getFirstName(){
        return firstName == null ? "" : firstName;
    }

    public String getLastName(){
        return lastName == null ? "" : lastName;
    }

    public String getChatIdAsString(){
        return chatId != null ? chatId.toString() : "";
    }

}
