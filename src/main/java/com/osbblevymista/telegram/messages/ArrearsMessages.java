package com.osbblevymista.telegram.messages;

import com.osbblevymista.telegram.miydim.ArrearsMiyDimProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;

public class ArrearsMessages extends OSBBMessage {

    public ArrearsMessages(ArrearsMiyDimProcessor arrearsMiyDim) {
        messages = new HashMap<>();

        HashMap<String, String> payments = arrearsMiyDim.getPayments();
        StringBuffer arrears = new StringBuffer();
        arrears.append("<b>Ваша заборгованість становить: </b>\n");

        payments.forEach((key, value) -> arrears.append(key)
                .append(" : ")
                .append(value)
                .append("грн.\n"));

        messages.put(ARREARS.ARREARS.value, String.valueOf(arrears));
    }

    @Override
    public String getMessage(String type) {
        return messages.get(type);
    }

    @AllArgsConstructor
    @Getter
    public enum ARREARS {
        ARREARS("arrears");

        String value;
    }


}
