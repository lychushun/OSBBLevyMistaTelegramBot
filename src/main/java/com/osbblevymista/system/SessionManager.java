package com.osbblevymista.system;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.filereaders.UserInfoFileReader;
import com.osbblevymista.models.UserInfo;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class SessionManager {

    public static String getSessionLogin(Optional<Session> optional){
        if (optional.isPresent()) {
            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
        }
        return "";
    }

    public static String getSessionPass(Optional<Session> optional){
        if (optional.isPresent()) {
            return (String) optional.get().getAttribute(SessionAttributes.PASS);
        }
        return "";
    }

    public static void addLoginAndPassToSession(Optional<Session> optional, Message message, UserInfoFileReader fileWorker) throws IOException, CsvException {

        if (optional.isPresent()) {

            UserInfo userInfo = new UserInfo();

            userInfo.setLastName(message.getChat().getLastName());
            userInfo.setFirstName(message.getChat().getFirstName());
            userInfo.setChatId(message.getChat().getId().toString());
            userInfo.setUserId(message.getFrom().getId());
            userInfo.setUserName(message.getFrom().getUserName());

            Session session = optional.get();
            boolean isAddedChatId = isAddedChatId(userInfo, fileWorker);
            if (isAddedChatId && Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED))) {
                session.setAttribute(SessionAttributes.IS_ADDED, true);
            } else if (Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED)) || !isAddedChatId) {
                Boolean isAdded = addChatId(userInfo, fileWorker);
                session.setAttribute(SessionAttributes.IS_ADDED, true);
            }
        }
    }

    private static boolean isAddedChatId(UserInfo userInfo, UserInfoFileReader fileWorker) throws IOException, CsvException {
        return fileWorker.isAddedUserInfo(userInfo);
    }

    private static boolean addChatId(UserInfo userInfo, UserInfoFileReader fileWorker) throws IOException, CsvException {
        return fileWorker.add(userInfo);
    }
}
