package com.osbblevymista.api.services;


import com.gargoylesoftware.htmlunit.CookieManager;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.api.CookiesManager;
import com.osbblevymista.api.dto.request.AdminInfoRequest;
import com.osbblevymista.api.dto.response.AdminInfoResponse;
import com.osbblevymista.api.dto.response.UserInfoResponse;
import com.osbblevymista.api.mappings.AdminMapper;
import com.osbblevymista.api.mappings.UserMapper;
import com.osbblevymista.telegram.miydim.MiyDimProcessor;
import com.osbblevymista.telegram.services.AdminInfoService;
import com.osbblevymista.telegram.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.util.StringUtils.hasText;

@Service
@RequiredArgsConstructor
public class MiyDimService {

    private final Logger logger = LoggerFactory.getLogger(MiyDimProcessor.class);
    private final CookiesManager cookiesManager;
    private final AdminInfoService adminInfoService;
    private final UserInfoService userInfoService;
    private final AdminMapper adminMapper;
    private final UserMapper userMapper;

    public String getCookie(String chatId) {
        return cookiesManager.getCookie(chatId);
    }

    public void deleteCookie(String chatId) {
        cookiesManager.removeCookies(chatId);
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

    public boolean addAdmin(AdminInfoRequest adminInfoRequest) {
        try {
            adminInfoService.addRow(adminMapper.adminInfoResponseToAdminInfo(adminInfoRequest));
            return true;
        } catch (CsvRequiredFieldEmptyException | CsvValidationException | CsvDataTypeMismatchException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean delete(Long adminId) {
           return adminInfoService.delete(adminId);
    }

    public List<AdminInfoResponse> getAllAdmins() {
        return adminInfoService.getAll().stream().map(adminMapper::adminInfoToAdminInfoResponse)
                .collect(Collectors.toList());
    }

    public List<UserInfoResponse> getAllUsers() {
        return userInfoService.getAll().stream().map(userMapper::userInfoToUserInfoResponse)
                .collect(Collectors.toList());
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
