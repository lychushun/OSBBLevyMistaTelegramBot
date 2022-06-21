package com.osbblevymista.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.models.AdminInfo;
import com.osbblevymista.models.AuthInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.text.ParseException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Component
public class AuthFileReader  extends FileReader<AuthInfo>{

    private AuthInfo authInfo;

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
        List<AuthInfo> authInfos = getAll();

        if (noneMatch(authInfos, this.authInfo)) {
            super.writeToFile(this.authInfo);
            return true;
        }
        return false;
    }

    @Override
    public List<AuthInfo> getAll(boolean force) throws IOException {
        return readFromFile(AuthInfo.class);
    }

    @Override
    protected boolean noneMatch(List<AuthInfo> userInfoList, AuthInfo uInfo) {
        return userInfoList.stream().noneMatch(item -> match(item, uInfo) || !item.isActive());
    }

    @Override
    protected boolean match(AuthInfo el1, AuthInfo el2) {
        return el1.getUserId() == el2.getUserId();
    }

    public Optional<AuthInfo> getFirstActiveUser(AuthInfo authInfo) throws IOException {
        Optional<AuthInfo> userInfo = Optional.empty();

        List<AuthInfo> authInfoList = get(authInfo);
        if (authInfoList.size() > 0) {
            return authInfoList.stream().sorted(Comparator.comparingLong(item -> {
                try {
                    return item.getCreateTsLong();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            })).filter(AuthInfo::isActive).findFirst();
        }
        return userInfo;
    }

}
