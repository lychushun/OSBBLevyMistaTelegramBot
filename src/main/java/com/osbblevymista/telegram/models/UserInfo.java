package com.osbblevymista.telegram.models;


import com.opencsv.bean.CsvBindByName;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    @CsvBindByName(column="CREATEDATETS", required = true)
    private String createDateTs;

    @CsvBindByName(column="APARTMENT")
    private String apartment = new String("-");

    @CsvBindByName(column="HOUSE")
    private String house = new String("-");

    @CsvBindByName(column="USERNAME")
    private String userName = new String("-");

    @CsvBindByName(column="FIRSTNAMEMD")
    private String firstNameMD = new String("-");

    @CsvBindByName(column="LASTNAMEMD")
    private String lastNameMD = new String("-");

    public UserInfo(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        createDateTs = dateFormat.format(date);

        lastNameMD = "-";
        firstNameMD = "-";
        userName = "-";
        house = "-";
        apartment = "-";
        lastName = "-";
    }

    public void setFirstName(String name){
        if (StringUtils.isEmpty(name)){
            this.firstName = "-";
        } else {
            this.firstName = name;
        }
    }

    public void setLastName(String name){
        if (StringUtils.isEmpty(name)){
            this.lastName = "-";
        } else {
            this.lastName = name;
        }
    }

    public void setUserName(String name){
        if (StringUtils.isEmpty(name)){
            this.userName = "-";
        } else {
            this.userName = name;
        }
    }

    @Override
    public String[] getHeaders(){
        return new String[]{"chatId", "firstName", "lastName", "sentNotifications", "userId", "createDate", "apartment", "house", "userName", "firstNameMD", "lastNameMD"};
    }

    @Override
    public String[] getAsArray() {
        return new String[]{
                apartment,
                chatId,
                createDateTs,
                firstName,
                firstNameMD,
                house,
                lastName,
                lastNameMD,
                String.valueOf(sentNotifications),
                String.valueOf(userId),
                userName
        };
    }

}
