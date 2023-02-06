package com.osbblevymista.telegram.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;


@Data
public class AdminInfo implements Info {

    @CsvBindByName(column = "FIRSTNAME", required = true)
    private String firstName;

    @CsvBindByName(column = "LASTNAME")
    private String lastName;

    @CsvBindByName(column = "ADMINID", required = true)
    private Long adminId;

    @CsvBindByName(column = "ACTIVE")
    private boolean active;

    @Override
    public String[] getHeaders() {
        return new String[]{"firstName", "lastName", "adminId", "active"};
    }

    @Override
    public String[] getAsArray() {
        return new String[]{firstName, lastName, adminId.toString(), String.valueOf(active)};
    }
//    759291097

}
