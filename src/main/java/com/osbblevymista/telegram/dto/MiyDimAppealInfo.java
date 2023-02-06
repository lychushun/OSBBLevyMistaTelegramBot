package com.osbblevymista.telegram.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MiyDimAppealInfo {

    private List<String> messages = new ArrayList<>();
    private List<byte[]> photos = new ArrayList<>();

    public String formatMessages(){
        return String.join("\n", messages);
    }

}
