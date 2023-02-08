package com.osbblevymista.api.dto.response;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;

@Data
public class UserInfoResponse {

    private String chatId;
    private String firstName;
    private String lastName;
    private boolean sentNotifications;
    private long userId;
    private String createDateTs;
    private String lastActiveDateTs;
    private String apartment;
    private String house;
    private String userName;
    private String firstNameMD;
    private String lastNameMD;

}
