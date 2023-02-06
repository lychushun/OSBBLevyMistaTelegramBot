package com.osbblevymista.telegram.utils;

import com.osbblevymista.telegram.filereaders.AuthFileReader;
import com.osbblevymista.telegram.models.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
@Component
public class AuthUtil {

    private final AuthFileReader authFileReader;

    public String getSessionLogin(long userId) throws IOException {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);

        Optional<AuthInfo> optional = authFileReader.getFirstActiveUser(authInfo);
        if (optional.isPresent()){
            return optional.get().getLogin();
        } else {
            return "";
        }
    }

    public String getSessionPass(long userId) throws IOException {
        AuthInfo authInfo = new AuthInfo();
        authInfo.setUserId(userId);

        Optional<AuthInfo> optional = authFileReader.getFirstActiveUser(authInfo);
        if (optional.isPresent()){
            return optional.get().getPass();
        } else {
            return "";
        }

    }

}
