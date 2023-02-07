package com.osbblevymista.api;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class CookiesManager {

    private final Map<String, String> cookies;

    public void addCookies(String id, String cookie){
        cookies.put(id, cookie);
    }

    public String getCookie(String chatId){
        return cookies.get(chatId);
    }

    public void removeCookies(String id){
        cookies.remove(id);
    }

    public boolean hasCookieId(String id){
        return cookies.containsKey(id);
    }

    public boolean hasCookie(String cookie){
        return cookies.containsValue(cookie);
    }

}
