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

    @CsvBindByName(column="CHAT_ID", required = true)
    private String chatId;

    @CsvBindByName(column="FIRST_NAME", required = true)
    private String firstName = "";

    @CsvBindByName(column="LAST_NAME")
    private String lastName = new String("-");

    @CsvBindByName(column="SENT_NOTIFICATION")
    private boolean sentNotifications = true;

    @CsvBindByName(column="USER_ID", required = true)
    private long userId;

    @CsvBindByName(column="CREATE_DATE_TS", required = true)
    private String createDateTs;

    @CsvBindByName(column="LAST_ACTIVE_DATE_TS", required = true)
    private String lastActiveDateTs;

    @CsvBindByName(column="APARTMENT")
    private String apartment = new String("-");

    @CsvBindByName(column="HOUSE")
    private String house = new String("-");

    @CsvBindByName(column="USER_NAME")
    private String userName = new String("-");

    @CsvBindByName(column="FIRST_NAME_MD")
    private String firstNameMD = new String("-");

    @CsvBindByName(column="LAST_NAME_MD")
    private String lastNameMD = new String("-");

    public UserInfo(){
        createDateTs = getCurrentDateTs();
        lastActiveDateTs = createDateTs;

        lastNameMD = "-";
        firstNameMD = "-";
        userName = "-";
        house = "-";
        apartment = "-";
        lastName = "-";
    }

    public void updateLastActiveDate(){
        lastActiveDateTs = getCurrentDateTs();
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
        return new String[]{"chat_Id", "first_Name", "last_Name", "sent_Notifications", "user_Id", "create_Date", "last_Active_Date", "apartment", "house", "user_Name", "first_Name_MD", "last_Name_MD"};
    }

    @Override
    public String[] getAsArray() {
        return new String[]{
                apartment,
                chatId,
                createDateTs,
                lastActiveDateTs,
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

    private String getCurrentDateTs(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        return dateFormat.format(date);
    }

}
