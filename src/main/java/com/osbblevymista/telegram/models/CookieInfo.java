//package com.osbblevymista.telegram.models;
//
//import com.opencsv.bean.CsvBindByName;
//import lombok.Data;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Map;
//
//@Data
//public class CookieInfo implements Info{
//
//    @CsvBindByName(column="USERID", required = true)
//    private long userId;
//
//    @CsvBindByName(column = "HJ_SESSION", required = true)
//    private String hjSession;
//
//    @CsvBindByName(column="CREATEDATETS", required = true)
//    private String createDateTs;
//
//    @Override
//    public String[] getHeaders() {
//        return new String[0];
//    }
//
//    @Override
//    public String[] getAsArray() {
//        return new String[]{String.valueOf(userId), createDateTs, hjSession};
//    }
//
//    public long getCreateTsLong() throws ParseException {
//        return new SimpleDateFormat("yyyy-mm-dd hh:mm:ss").parse(this.createDateTs).getTime();
//    }
//}
