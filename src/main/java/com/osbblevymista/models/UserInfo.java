package com.osbblevymista.models;


import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class UserInfo implements Info {

    @CsvBindByName(column="CHATID", required = true)
    private String chatId;

    @CsvBindByName(column="FIRSTNAME", required = true)
    private String firstName = "";

    @CsvBindByName(column="LASTNAME")
    private String lastName = new String("-");

    @CsvBindByName(column="SENTNOTIFICATION")
    private boolean sentNotifications = true;

    @CsvBindByName(column="USERID", required = true)
    private long userId;

    @Override
    public String[] getHeaders(){
        return new String[]{"chatId", "firstName", "lastName", "sentNotifications", "userId"};
    }

    @Override
    public String[] getAsArray() {
        return new String[]{chatId, firstName, lastName, String.valueOf(sentNotifications), String.valueOf(userId)};
    }

}
