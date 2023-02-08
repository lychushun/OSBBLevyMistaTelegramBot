package com.osbblevymista.telegram.services;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Service
@RequiredArgsConstructor
public class UserInfoService extends FileService<UserInfo, String>{

    private Map<String, UserInfo> userInfoMap;
    private final UserInfoFileReader userInfoFileReader;

    @Override
    public void addRow(UserInfo userInfo){
        if (userInfoMap.containsKey(userInfo.getChatId())){
            UserInfo userInfo1 = userInfoMap.get(userInfo.getChatId());
            userInfo = marge(userInfo, userInfo1);
        }
        userInfoMap.put(userInfo.getChatId(), userInfo);
    }

    @Override
    public UserInfo getRow(String chatId) {
        return userInfoMap.get(chatId);
    }

    @PostConstruct
    private void initUserInfo() throws IOException {
        List<UserInfo> userInfoList = userInfoFileReader.getAll(true);
        userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getChatId, identity()));
    }

    @Override
    public void doSnapshot(){
        userInfoFileReader.deleteFile();
        userInfoFileReader.add(new ArrayList<>(userInfoMap.values()));
    }

    @Override
    public int size(){
        if (userInfoMap != null){
            return userInfoMap.size();
        }
        return 0;
    }

    @Override
    public Collection<UserInfo> getAll() {
        return userInfoMap.values();
    }

    @Override
    public boolean delete(String id) {
        if (userInfoMap.containsKey(id)){
            userInfoMap.remove(id);
            doSnapshot();
            return true;
        }
        return false;
    }

    private UserInfo marge(UserInfo userInfo1, UserInfo userInfo2){
        UserInfo userInfo = userInfo1;
        userInfo.updateLastActiveDate();
        userInfo.setFirstName(userInfo2.getFirstName());
        userInfo.setLastName(userInfo2.getLastName());
        userInfo.setSentNotifications(userInfo2.isSentNotifications());

        userInfo.setLastNameMD(userInfo2.getLastNameMD());
        userInfo.setFirstNameMD(userInfo2.getFirstName());
        userInfo.setUserName(userInfo2.getUserName());
        userInfo.setHouse(userInfo2.getHouse());
        userInfo.setApartment(userInfo2.getApartment());

        return userInfo;
    }
}
