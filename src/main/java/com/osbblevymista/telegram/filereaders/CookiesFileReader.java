//package com.osbblevymista.telegram.filereaders;
//
//import com.opencsv.exceptions.CsvDataTypeMismatchException;
//import com.opencsv.exceptions.CsvException;
//import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
//import com.opencsv.exceptions.CsvValidationException;
//import com.osbblevymista.telegram.models.AuthInfo;
//import com.osbblevymista.telegram.models.CookieInfo;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.text.ParseException;
//import java.util.Date;
//import java.util.List;
//import java.util.Optional;
//
//@Component
//public class CookiesFileReader extends FileReader<CookieInfo>{
//
//    @Value("${auth.refresh.info}")
//    private long REFRESH_TIME_OUT;
//
//    private CookieInfo cookieInfo;
//    private Date createDate = new Date();
//    private List<CookieInfo> authInfos;
//
//    @Override
//    public String getFileName() {
//        return "cookiesInfo.csv";
//    }
//
//    public boolean add(long userId, String hjSession) throws IOException, CsvException {
//        this.cookieInfo = new CookieInfo();
//        cookieInfo.setHjSession(hjSession);
//        return add();
//    }
//
//    @Override
//    protected boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
//        List<CookieInfo> userInfoList = getAll();
//
//        logger.info("INSERTING authInfo: " + cookieInfo.toString());
//
//        Optional<CookieInfo> firstActive = getFirstActiveUser(userInfoList);
//
//        if (firstActive.isPresent()) {
//            if (!fullMatch(firstActive.get(), this.cookieInfo)) {
//                super.writeToFile(cookieInfo);
//                getAll(true);
//                logger.info("authInfo was INSERTED: " + cookieInfo.toString());
//                return true;
//            }
//        }
//
//        logger.info("authInfo was not INSERTED: " + cookieInfo.toString());
//        return false;
//    }
//
//    @Override
//    public List<CookieInfo> getAll(boolean force) throws IOException {
//        if (authInfos == null || refresh() || force) {
//            logger.info("Getting info about auth.");
//            authInfos = readFromFile(CookieInfo.class);
//        }
//        return authInfos;
//    }
//
//    @Override
//    protected boolean noneMatch(List<CookieInfo> userInfoList, CookieInfo uInfo) {
//        return false;
//    }
//
//    @Override
//    protected boolean match(CookieInfo el1, CookieInfo el2) {
//        return false;
//    }
//
//    private boolean refresh(){
//        if ((createDate.getTime() + REFRESH_TIME_OUT) <= new Date().getTime()){
//            createDate = new Date();
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    protected boolean fullMatch(CookieInfo el1, CookieInfo el2) {
//        return el1.getUserId() == el2.getUserId()
//                && el1.getHjSession().equals(el2.getHjSession());
//    }
//
//    public Optional<CookieInfo> getFirstActiveUser(CookieInfo authInfo) throws IOException {
//        List<CookieInfo> authInfoList = get(authInfo);
//        return getFirstActiveUser(authInfoList);
//    }
//
//    public Optional<CookieInfo> getLastActiveUser(List<CookieInfo> authInfoList) throws IOException {
//        Optional<CookieInfo> userInfo = Optional.empty();
//
//        if (authInfoList.size() > 0) {
//            return authInfoList.stream().sorted((item1, item2) -> {
//                try {
//                    return Math.toIntExact(item2.getCreateTsLong() - item1.getCreateTsLong());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                return 0;
//            }).filter(CookieInfo::isActive).findFirst();
//        }
//        return userInfo;
//    }
//
//    public Optional<CookieInfo> getFirstActiveUser(List<CookieInfo> authInfoList) throws IOException {
//        Optional<CookieInfo> userInfo = Optional.empty();
//
//        if (authInfoList.size() > 0) {
//            return authInfoList.stream().sorted((item1, item2) -> {
//                try {
//                    return Math.toIntExact(item2.getCreateTsLong() - item1.getCreateTsLong());
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                return 0;
//            }).filter(CookieInfo::isActive).findFirst();
//        }
//        return userInfo;
//    }
//}
