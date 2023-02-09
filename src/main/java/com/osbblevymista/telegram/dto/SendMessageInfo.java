package com.osbblevymista.telegram.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class SendMessageInfo {

    private List<String> messages = new ArrayList<>();

    public String formatMessages(){
        return String.join("\n", messages);
    }

}
