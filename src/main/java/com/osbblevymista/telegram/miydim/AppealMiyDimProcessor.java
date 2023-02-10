package com.osbblevymista.telegram.miydim;

import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AppealMiyDimProcessor extends MiyDimProcessor{
    public AppealMiyDimProcessor(String cookie) {
        super(cookie);
    }

    public List<Appeal> getAppeals() throws IOException {
//        HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/41035710/uk/order");
        HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/order");
        List<HtmlSection> htmlSections = page1.getByXPath("//div[@id='ResidentAreaAppeal']/section");//news contents

        return (List<Appeal>) htmlSections.stream().map(item -> {
            DomElement dateTime = item.getElementsByTagName("header").get(0);
            DomElement message = item.getElementsByTagName("p").get(0);

            String strDateTime = dateTime.getVisibleText();

            HtmlSpan domElement = (HtmlSpan)item.getElementsByTagName("span").get(0);
            String htmlClassStatus = domElement.getAttribute("class");

            return Appeal.builder()
                    .createDateTime(strDateTime)
                    .message(message.getVisibleText())
                    .appeal_status(Appeal.APPEAL_STATUS.fromHTMLClass(htmlClassStatus))
                    .build();
                }).collect(Collectors.toList());
    }

    public Optional<String> createAppeal(String text) throws IOException {
//        HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/41035710/uk/order/create");
        HtmlPage page1 = webClient.getPage("https://miydimonline.com.ua/order/create");

        List<HtmlForm> listF = page1.getForms();
        HtmlForm form = listF.stream().filter(item -> {
            return item.getAttribute("class").equals("form-create-appeal");
        }).findFirst().get();

        HtmlTextArea htmlTextArea = form.getTextAreaByName("Body");
        htmlTextArea.setText(text);

        HtmlSelect htmlSelect = form.getSelectByName("TechnicalAccistanceId");
        HtmlOption option = htmlSelect.getOptionByValue("1");
        htmlSelect.setSelectedAttribute(option, true);

        HtmlButton button = form.getButtonByName("Save");
        HtmlPage page2 = button.click();

//        if (page2.getUrl().getPath().equals("/41035710/uk/order")){
        if (page2.getUrl().getPath().contains("/order")){
            return Optional.of(getLastRequest(page2));
        } else {
            return Optional.empty();
        }
    }

    private String getLastRequest(HtmlPage orderPage){
        List<HtmlAnchor> links = orderPage.getByXPath("//a[@class='appeal-comments']");
        HtmlAnchor link = links.get(0);
        return "https://miydimonline.com.ua" + link.getHrefAttribute();
    }
}
