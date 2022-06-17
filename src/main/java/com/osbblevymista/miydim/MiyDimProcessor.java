package com.osbblevymista.miydim;

import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import lombok.Getter;

import java.util.List;
import java.util.Objects;

@Getter
public class MiyDimProcessor {

    protected HtmlPage currentPage;
    protected HtmlPage mainPage;

    protected WebClient webClient;

    private String login;
    private String pass;

    private String errorMessage = "";
    private Boolean isLogin = false;

    public MiyDimProcessor(String login, String pass){
        this.login = login;
        this.pass = pass;
        logIn(login, pass);
    }

    protected void logIn(String login, String pass){
        if (Objects.isNull(login) || Objects.isNull(pass)){
            isLogin = false;
        } else {
            webClient = new WebClient();
            CookieManager cookieMan = new CookieManager();
            cookieMan = webClient.getCookieManager();
            cookieMan.setCookiesEnabled(true);

            webClient.setCookieManager(cookieMan);

            webClient.getOptions().setThrowExceptionOnScriptError(false);

            try {
                HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/41035710/uk/account/login");

                List<HtmlForm> listF = page1.getForms();
                HtmlForm form = listF.get(0);

                HtmlEmailInput uName = form.getInputByName("Email");
                HtmlPasswordInput passWord = form.getInputByName("Password");

                HtmlButton button = form.getFirstByXPath("//div[contains(@class, 'submit-group')]/button");
                uName.setValueAttribute(login);
                passWord.setValueAttribute(pass);
                HtmlPage page2 = button.click();

                currentPage = page2.getPage();
                mainPage = page2.getPage();

                HtmlListItem item = currentPage.getFirstByXPath("//div[contains(@class, 'validation-summary-errors')]/ul/li");

                isLogin = Objects.isNull(item);
                errorMessage = Objects.nonNull(item) ? item.getFirstChild().asNormalizedText() : "";
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void refresh(){
        logIn(login, pass);
    }


}
