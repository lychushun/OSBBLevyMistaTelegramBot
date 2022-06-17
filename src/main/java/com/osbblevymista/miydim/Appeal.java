package com.osbblevymista.miydim;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.HashMap;

@Getter
@Builder
public class Appeal {

    String createDateTime;
    String message;
    APPEAL_STATUS appeal_status;


    @Getter
    @AllArgsConstructor
    public enum APPEAL_STATUS{
        COMPLETED ("Завершено"),
        PENDING ("Відкрито"),
        INPROGRESS ("В процесі");

        private String value;

        private static final HashMap<String, APPEAL_STATUS> map = new HashMap<>();

        static {
            map.put("technical-status-closed", COMPLETED);
            map.put("technical-status-open", PENDING);
            map.put("technical-status-inprocess", INPROGRESS);
        }

        public static APPEAL_STATUS fromHTMLClass(String htmlClass){
            return map.get(htmlClass);
        }

        public String toString(){
            return this.value;
        }
    }

}
