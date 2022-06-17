package com.osbblevymista.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.models.UserInfo;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class UserInfoFileReader extends FileReader<UserInfo> {

    private static UserInfoFileReader userInfoFileReader;

    private UserInfo userInfo;
    public List<UserInfo> userInfoList;

    private final long REFRESH_TIME_OUT = 30000;

    private Date createDate = new Date();

    public static UserInfoFileReader createInstance(){
        if (Objects.isNull(userInfoFileReader)){
            userInfoFileReader = new UserInfoFileReader();
        }
        return userInfoFileReader;
    }

    private UserInfoFileReader() {
        super(UserInfo.class, "userInfo.csv");
    }

    public boolean add(UserInfo userInfo) throws IOException, CsvException {
        this.userInfo = userInfo;
        return add();
    }

    @Override
    protected boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
        List<UserInfo> userInfoList = get();

        if (noneMatch(userInfoList, this.userInfo)) {
            super.writeToFile(userInfo);
            get();
            return true;
        }

        return false;
    }

    @Override
    public List<UserInfo> get() throws IOException {
        if (userInfoList == null || refresh()) {
            userInfoList = readFromFile();
        }
        return userInfoList;
    }

    public boolean isAddedUserInfo(UserInfo uInfo) throws IOException {
        List<UserInfo> userInfoList = get();
        return !noneMatch(userInfoList, uInfo);
    }

    private boolean refresh(){
        if ((createDate.getTime() + REFRESH_TIME_OUT) <= new Date().getTime()){
            createDate = new Date();
            return true;
        } else {
            return false;
        }
    }

    private boolean noneMatch(List<UserInfo> userInfoList, UserInfo uInfo){
        return userInfoList.stream().noneMatch(item -> item.getChatId().equals(uInfo.getChatId())
                && item.getUserId() == uInfo.getUserId());
    }
}
