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

    @Override
    public String getFileName(){
        return "userInfo.csv";
    }

    public void add(List<UserInfo> userInfo) {
        if(userInfo!=null){
            userInfo.forEach(el ->{
                try {
                    super.writeToFile(el);
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | CsvValidationException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    @Override
    public List<UserInfo> getAll(boolean force) throws IOException {
        return readFromFile(UserInfo.class);
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
