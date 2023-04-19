package com.osbblevymista.miydim;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

public class ArrearsMiyDimProcessor extends MiyDimProcessor{
    public ArrearsMiyDimProcessor(String cookie) {
        super(cookie);
    }

    public HashMap<String, String> getPayments(){
        HtmlTable table = mainPage.getFirstByXPath("//table");

        HashMap<String, String> payment = new HashMap<>();

        for (final HtmlTableRow row : table.getRows()) {
            List<HtmlTableCell> cells = row.getCells();

            if (cells.size() == 2) {
                String amount = cells.get(1).asNormalizedText();
                amount = amount.replaceAll("\\D+", "");
                if (StringUtils.isNotEmpty(amount)) {
                    float f = Float.parseFloat(amount) / 100;
                    payment.put(cells.get(0).asNormalizedText(), f + "");
                }
            }
        }

        return payment;
    }

    public String getPrivat24PaymentLink() throws UnsupportedEncodingException, URISyntaxException {
        List<HtmlAnchor> links = mainPage.getByXPath("//a[contains(@class, 'panel-payment-bank')]");
        HtmlAnchor link = links.get(0);
        return encodeURL(link.getHrefAttribute());
    }

    public String getIPayPaymentLink() throws UnsupportedEncodingException {
        List<HtmlAnchor> links = mainPage.getByXPath("//a[contains(@class, 'panel-payment-bank')]");
        HtmlAnchor link = links.get(1);
        return link.getHrefAttribute();
    }

    private String encodeURL(String originalURL) throws UnsupportedEncodingException {
        String arr[] = originalURL.split("/");
        String query = arr[arr.length-1];
        arr = ArrayUtils.remove(arr, arr.length-1);
        String path = String.join("/", arr);
        return path + "/" +  URLEncoder.encode(query, StandardCharsets.UTF_8.name());
    }
}
