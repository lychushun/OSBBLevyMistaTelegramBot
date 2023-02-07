package com.osbblevymista.api.services;


import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.osbblevymista.api.CookiesManager;
import com.osbblevymista.telegram.miydim.MiyDimProcessor;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class MiyDimService {

    private final Logger logger = LoggerFactory.getLogger(MiyDimProcessor.class);
    private final CookiesManager cookiesManager;

    public String getCookie(String chatId){
        return cookiesManager.getCookie(chatId);
    }

    public Optional<String> auth(String login, String pass, String chatId) {
        try {
            String cookie = logIn(login, pass);
            cookiesManager.addCookies(chatId, cookie);
            return Optional.empty();
        } catch (Exception e) {
            logger.warn("Can not login. Message - " + e.getMessage());
            return Optional.of(e.getMessage());
        }
    }

    private String logIn(String login, String pass) throws Exception {
        if (Objects.isNull(login) || Objects.isNull(pass)) {
            throw new Exception("Login or pass is missing");
        } else {
            WebClient webClient = new WebClient();
            CookieManager cookieMan = new CookieManager();
            cookieMan = webClient.getCookieManager();
            cookieMan.setCookiesEnabled(true);

            webClient.setCookieManager(cookieMan);

            webClient.getOptions().setThrowExceptionOnScriptError(false);

            HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/41035710/uk/account/login");

            List<HtmlForm> listF = page1.getForms();
            HtmlForm form = listF.get(0);

            HtmlEmailInput uName = form.getInputByName("Email");
            HtmlPasswordInput passWord = form.getInputByName("Password");

            HtmlButton button = form.getFirstByXPath("//div[contains(@class, 'submit-group')]/button");
            uName.setValueAttribute(login);
            passWord.setValueAttribute(pass);
            HtmlPage page2 = button.click();

            HtmlPage currentPage = page2.getPage();
            HtmlListItem item = currentPage.getFirstByXPath("//div[contains(@class, 'validation-summary-errors')]/ul/li");
            String errorMessage = Objects.nonNull(item) ? item.getFirstChild().asNormalizedText() : "";
            if (hasText(errorMessage)) {
                throw new Exception(errorMessage);
            } else {
                return getAspNetApplicationCookie(currentPage);
            }
        }
    }

    private String getAspNetApplicationCookie(HtmlPage currentPage) throws Exception {
        if (currentPage != null && currentPage.getWebClient() != null
                && currentPage.getWebClient().getCookieManager() != null && currentPage.getWebClient().getCookieManager().getCookies() != null) {
            return currentPage.getWebClient().getCookieManager().getCookie(".AspNet.ApplicationCookie").getValue();
        } else {
            throw new Exception("Проблема з автризацію.");
        }
    }

}
