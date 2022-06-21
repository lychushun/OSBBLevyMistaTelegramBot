package com.osbblevymista.models;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Data
public class AuthInfo implements Info{

    @CsvBindByName(column="USERID", required = true)
    private long userId;

    @CsvBindByName(column = "PASS", required = true)
    private String pass;

    @CsvBindByName(column = "LOGIN", required = true)
    private String login;

    @CsvBindByName(column = "ACTIVE", required = true)
    private boolean active;

    @CsvBindByName(column="CREATEDATETS", required = true)
    private String createDateTs;

    @Override
    public String[] getHeaders() {
        return new String[0];
    }

    @Override
    public String[] getAsArray() {
        return new String[]{String.valueOf(active), createDateTs, login, pass, String.valueOf(userId)};
    }

    public AuthInfo(){
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        createDateTs = dateFormat.format(date);
        active = true;
    }

    public Date getCreateTs() throws ParseException {
        return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.createDateTs);
    }

    public long getCreateTsLong() throws ParseException {
        return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.createDateTs).getTime();
    }

}
