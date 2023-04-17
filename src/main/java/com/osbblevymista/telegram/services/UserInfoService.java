package com.osbblevymista.telegram.services;

import com.osbblevymista.OSBBLevyMista45;
import com.osbblevymista.telegram.filereaders.UserInfoFileReader;
import com.osbblevymista.telegram.models.UserInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
@RequiredArgsConstructor
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class UserInfoService extends FileService<UserInfo, String>{

    final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

    private Map<String, UserInfo> userInfoMap;
    private final UserInfoFileReader userInfoFileReader;

    @PostConstruct
    private void initUserInfo() throws IOException {
        List<UserInfo> userInfoList = userInfoFileReader.getAll(true);
        userInfoMap = userInfoList.stream().collect(Collectors.toMap(UserInfo::getChatId, identity()));
    }

    @Override
    public void addRow(UserInfo userInfo){
        if (userInfoMap.containsKey(userInfo.getChatId())){
            UserInfo userInfo1 = userInfoMap.get(userInfo.getChatId());
            userInfo = marge(userInfo1, userInfo);
        }
        userInfoMap.put(userInfo.getChatId(), userInfo);
    }

    @Override
    public UserInfo getRow(String chatId) {
        return userInfoMap.get(chatId);
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
