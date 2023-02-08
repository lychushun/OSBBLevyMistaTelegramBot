package com.osbblevymista.telegram.services;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.Collection;

public abstract class FileService<R, T> {

    public abstract void addRow(R userInfo) throws CsvRequiredFieldEmptyException, CsvValidationException, CsvDataTypeMismatchException, IOException;
    public abstract R getRow(T id);

//    @PostConstruct
//    private void initUserInfo() throws IOException {
//        List<UserInfo> userInfoList = userInfoFileReader.getAll(true);
//        userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getChatId, identity()));
//    }

    public abstract void doSnapshot();
    public abstract int size();

    public abstract Collection<R> getAll();

    public abstract boolean delete(T id);

//    private UserInfo marge(UserInfo userInfo1, UserInfo userInfo2){
//        UserInfo userInfo = userInfo1;
//        userInfo.updateLastActiveDate();
//        userInfo.setFirstName(userInfo2.getFirstName());
//        userInfo.setLastName(userInfo2.getLastName());
//        userInfo.setSentNotifications(userInfo2.isSentNotifications());
//
//        userInfo.setLastNameMD(userInfo2.getLastNameMD());
//        userInfo.setFirstNameMD(userInfo2.getFirstName());
//        userInfo.setUserName(userInfo2.getUserName());
//        userInfo.setHouse(userInfo2.getHouse());
//        userInfo.setApartment(userInfo2.getApartment());
//
//        return userInfo;
//    }

}
