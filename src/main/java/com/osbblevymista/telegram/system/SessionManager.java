package com.osbblevymista.telegram.system;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionManager {

//    private final PasswordUtil passwordUtil;
//    private final AuthFileReader authFileReader;

//    public String getSessionLogin(long userId) throws IOException {
//        AuthInfo authInfo = new AuthInfo();
//        authInfo.setUserId(userId);
//
//        Optional<AuthInfo> optional = authFileReader.getFirstActiveUser(authInfo);
//        if (optional.isPresent()){
//            return optional.get().getLogin();
//        } else {
//            return "";
//        }
//
//        //        if (optional.isPresent()) {
////            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
////        }
//    }
//
//    public String getSessionPass(long userId) throws IOException {
//        AuthInfo authInfo = new AuthInfo();
//        authInfo.setUserId(userId);
//
//        Optional<AuthInfo> optional = authFileReader.getFirstActiveUser(authInfo);
//        if (optional.isPresent()){
//            return optional.get().getPass();
//        } else {
//            return "";
//        }
//
////        if (optional.isPresent()) {
////            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
////        }
//    }

//    public String getSessionLogin(Optional<Session> optional){
//        if (optional.isPresent()) {
//            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
//        }
//        return "";
//    }
//
//    public String getSessionPass(Optional<Session> optional){
//        if (optional.isPresent()) {
//            return (String) optional.get().getAttribute(SessionAttributes.PASS);
//        }
//        return "";
//    }

//    public void addLoginAndPassToSession(Optional<Session> optional, Message message, UserInfoFileReader fileWorker) throws IOException, CsvException {
//
//        if (optional.isPresent()) {
//
//            UserInfo userInfo = new UserInfo();
//
//            userInfo.setLastName(message.getChat().getLastName());
//            userInfo.setFirstName(message.getChat().getFirstName());
//            userInfo.setChatId(message.getChat().getId().toString());
//            userInfo.setUserId(message.getFrom().getId());
//            userInfo.setUserName(message.getFrom().getUserName());
//
//            Session session = optional.get();
//            boolean isAddedChatId = isAddedChatId(userInfo, fileWorker);
//            if (isAddedChatId && Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED))) {
//                session.setAttribute(SessionAttributes.IS_ADDED, true);
//            } else if (Objects.isNull(session.getAttribute(SessionAttributes.IS_ADDED)) || !isAddedChatId) {
//                Boolean isAdded = addChatId(userInfo, fileWorker);
//                session.setAttribute(SessionAttributes.IS_ADDED, true);
//            }
//        }
//    }

//    private boolean isAddedChatId(UserInfo userInfo, UserInfoFileReader fileWorker) throws IOException, CsvException {
//        return fileWorker.contains(userInfo);
//    }
//
//    private boolean addChatId(UserInfo userInfo, UserInfoFileReader fileWorker) throws IOException, CsvException {
//        return fileWorker.add(userInfo);
//    }
}
