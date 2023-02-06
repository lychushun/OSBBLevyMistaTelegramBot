package com.osbblevymista.telegram.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.models.AuthInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
public class AuthFileReader extends FileReader<AuthInfo>{

    private AuthInfo authInfo;

    private List<AuthInfo> authInfos;

    private Date createDate = new Date();

    @Value("${auth.refresh.info}")
    private long REFRESH_TIME_OUT;

    public boolean add(long userId, String pass, String login) throws IOException, CsvException {
        this.authInfo = new AuthInfo();
        authInfo.setLogin(login);
        authInfo.setPass(pass);
        authInfo.setUserId(userId);
        return add();
    }

    @Override
    public String getFileName() {
        return "auth.csv";
    }

    @Override
    protected boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
        List<AuthInfo> userInfoList = getAll();

        logger.info("INSERTING authInfo: " + authInfo.toString());

        Optional<AuthInfo> firstActive = getFirstActiveUser(userInfoList);

        if (firstActive.isPresent()) {
            if (!fullMatch(firstActive.get(), this.authInfo)) {
                super.writeToFile(authInfo);
                getAll(true);
                logger.info("authInfo was INSERTED: " + authInfo.toString());
                return true;
            }
        }

        logger.info("authInfo was not INSERTED: " + authInfo.toString());
        return false;
    }

    @Override
    public List<AuthInfo> getAll(boolean force) throws IOException {
        if (authInfos == null || refresh() || force) {
            logger.info("Getting info about auth.");
            authInfos = readFromFile(AuthInfo.class);
        }
        return authInfos;
    }

    @Override
    protected boolean noneMatch(List<AuthInfo> userInfoList, AuthInfo uInfo) {
        return userInfoList.stream().noneMatch(item -> fullMatch(item, uInfo) || !item.isActive());
    }

    @Override
    protected boolean match(AuthInfo el1, AuthInfo el2) {
        return el1.getUserId() == el2.getUserId();
    }

    protected boolean fullMatch(AuthInfo el1, AuthInfo el2) {
        return el1.getUserId() == el2.getUserId()
                && el1.getPass().equals(el2.getPass())
                && el1.getLogin().equals(el2.getLogin());
    }

    public Optional<AuthInfo> getFirstActiveUser( AuthInfo authInfo) throws IOException {
        List<AuthInfo> authInfoList = get(authInfo);
        return getFirstActiveUser(authInfoList);
    }

    public Optional<AuthInfo> getFirstActiveUser(List<AuthInfo> authInfoList) throws IOException {
        Optional<AuthInfo> userInfo = Optional.empty();

        if (authInfoList.size() > 0) {
            return authInfoList.stream().sorted((item1, item2) -> {
                try {
                    return Math.toIntExact(item2.getCreateTsLong() - item1.getCreateTsLong());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }).filter(AuthInfo::isActive).findFirst();
        }
        return userInfo;
    }

    private boolean refresh(){
        if ((createDate.getTime() + REFRESH_TIME_OUT) <= new Date().getTime()){
            createDate = new Date();
            return true;
        } else {
            return false;
        }
    }

}
