package com.osbblevymista.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class PasswordUtil {

    @Value("auth.passsoult")
    private String passsoult;

    private final int iterations = 200000;
    private final int hashWidth = 256;

    public String encoder(String pass){
        Pbkdf2PasswordEncoder pbkdf2PasswordEncoder =
                new Pbkdf2PasswordEncoder(passsoult, iterations, hashWidth);
        pbkdf2PasswordEncoder.setEncodeHashAsBase64(true);
        return pbkdf2PasswordEncoder.encode(pass);
    }
}
