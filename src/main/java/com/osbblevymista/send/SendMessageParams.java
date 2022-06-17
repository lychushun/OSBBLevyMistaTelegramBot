package com.osbblevymista.send;


import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;

@Builder
@Getter
public class SendMessageParams {

    private Long chatId;

    private String login;
    private String pass;

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

}
