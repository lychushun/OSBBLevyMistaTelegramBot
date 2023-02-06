package com.osbblevymista.api.dto.request;


import lombok.Data;

@Data
public class AuthRequest {

    private String login;
    private String pass;
    private String chatId;

}
