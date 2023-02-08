package com.osbblevymista.telegram.models;

import com.opencsv.bean.CsvBindByName;
import lombok.Data;


@Data
public class AdminInfo implements Info {

    @CsvBindByName(column = "FIRST_NAME", required = true)
    private String firstName;

    @CsvBindByName(column = "LAST_NAME")
    private String lastName;

    @CsvBindByName(column = "ADMIN_ID", required = true)
    private Long adminId;

    @CsvBindByName(column = "ACTIVE")
    private boolean active;

    @Override
    public String[] getHeaders() {
        return new String[]{"ACTIVE", "ADMIN_ID", "FIRST_NAME", "LAST_NAME"};
    }

    @Override
    public String[] getAsArray() {
        return new String[]{String.valueOf(active), adminId.toString(), firstName, lastName};
    }
//    759291097

}
