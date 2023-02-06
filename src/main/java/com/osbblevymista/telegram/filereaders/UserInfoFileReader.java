package com.osbblevymista.telegram.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.models.UserInfo;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.List;

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
        List<UserInfo> userInfoList = getAll();

        logger.info("INSERTING userInfo: " + userInfo.toString());

        if (noneMatch(userInfoList, this.userInfo)) {
            super.writeToFile(userInfo);
            getAll(true);
            logger.info("userInfo was INSERTED: " + userInfo.toString());
            return true;
        }

        logger.info("userInfo was not INSERTED: " + userInfo.toString());
        return false;
    }

    @Override
    public List<UserInfo> getAll(boolean force) throws IOException {
        if (userInfoList == null || refresh() || force) {
            logger.info("Getting info about users.");
            userInfoList = readFromFile(UserInfo.class);
        }
        return userInfoList;
    }

//    @Override
//    public boolean contains(UserInfo el) throws IOException {
//        List<UserInfo> userInfoList = get();
//        logger.debug("isAddedUserInfo userInfoList: " + userInfoList);
//        boolean res = !noneMatch(userInfoList, el);
//        logger.debug("isAddedUserInfo: " + res);
//        return res;
//    }

    private boolean refresh(){
        if ((createDate.getTime() + REFRESH_TIME_OUT) <= new Date().getTime()){
            createDate = new Date();
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean noneMatch(List<UserInfo> userInfoList, UserInfo uInfo){
        logger.debug("userInfoList: " + userInfoList.toString());
        return userInfoList.stream().noneMatch(item -> match(item,uInfo)
                && item.getUserId() == uInfo.getUserId());
    }

    @Override
    protected boolean match(UserInfo el1, UserInfo el2) {
        return el1.getChatId().equals(el2.getChatId());
    }
}
