package com.osbblevymista.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Component
public class UserInfoFileReader extends FileReader<UserInfo> {

    private UserInfo userInfo;
    public List<UserInfo> userInfoList;

    private final long REFRESH_TIME_OUT = 30000;

    private Date createDate = new Date();

    @Override
    public String getFileName(){
        return "userInfo.csv";
    }

    public boolean add(UserInfo userInfo) throws IOException, CsvException {
        this.userInfo = userInfo;
        return add();
    }

    @Override
    protected boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
        List<UserInfo> userInfoList = get();

        logger.debug("INSERTING userInfo: " + userInfo.toString());

        if (noneMatch(userInfoList, this.userInfo)) {
            super.writeToFile(userInfo);
            get(true);
            logger.debug("userInfo was INSERTED: " + userInfo.toString());
            return true;
        }

        logger.debug("userInfo was not INSERTED: " + userInfo.toString());
        return false;
    }

    @Override
    public List<UserInfo> get(boolean force) throws IOException {
        if (userInfoList == null || refresh() || force) {
            logger.info("Getting info about users.");
            userInfoList = readFromFile(UserInfo.class);
        }
        return userInfoList;
    }

    public boolean isAddedUserInfo(UserInfo uInfo) throws IOException {
        List<UserInfo> userInfoList = get();
        logger.debug("isAddedUserInfo userInfoList: " + userInfoList);
        boolean res = !noneMatch(userInfoList, uInfo);
        logger.debug("isAddedUserInfo: " + res);
        return res;
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
        logger.debug("userInfoList: " + userInfoList.toString());
        return userInfoList.stream().noneMatch(item -> item.getChatId().equals(uInfo.getChatId())
                && item.getUserId() == uInfo.getUserId());
    }
}
