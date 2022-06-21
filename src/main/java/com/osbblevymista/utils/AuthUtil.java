package com.osbblevymista.utils;

import com.osbblevymista.filereaders.AuthFileReader;
import com.osbblevymista.models.AuthInfo;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.beans.factory.annotation.Value;
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

        //        if (optional.isPresent()) {
//            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
//        }
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

//        if (optional.isPresent()) {
//            return (String) optional.get().getAttribute(SessionAttributes.LOGIN);
//        }
    }

}
