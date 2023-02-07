package com.osbblevymista.telegram.miydim;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.util.Cookie;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

@Getter
public class MiyDimProcessor {

    private final Logger logger = LoggerFactory.getLogger(MiyDimProcessor.class);
    private final String domain = "miydimonline.com.ua";

    protected HtmlPage currentPage;
    protected HtmlPage mainPage;

    protected WebClient webClient;


    private String cookie;
    private Date expires = new Date();

    private String errorMessage = "";

    public MiyDimProcessor(String cookie){
        this.cookie = cookie;
        createCookieDate();

        webClient = new WebClient();

        CookieManager cookieMan = new CookieManager();
        cookieMan = webClient.getCookieManager();
        cookieMan.setCookiesEnabled(true);
        cookieMan.addCookie(getCookie());

        webClient.setCookieManager(cookieMan);

        webClient.getOptions().setThrowExceptionOnScriptError(false);

        try {
            mainPage = webClient.getPage("https://miydimonline.com.ua/residents/currentaccountstatus");
            currentPage = mainPage;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
        }
    }

    public boolean isLogin(){
        try {
            HtmlPage page = webClient.getPage("https://miydimonline.com.ua/residents/currentaccountstatus");
            return mainPage != null
                    && !page.getUrl().toString().contains("login")
                    && expires.getTime() >= new Date().getTime();
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(),e);
            return false;
        }
    }

    private Cookie getCookie(){
        return new Cookie(domain, ".AspNet.ApplicationCookie", this.cookie, "/", this.expires, false);
    }

    private void createCookieDate(){
        expires.setTime(expires.getTime() + (1 * 60 * 60 * 1000));
    }

}
