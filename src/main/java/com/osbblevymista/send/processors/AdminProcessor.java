package com.osbblevymista.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.filereaders.AdminFileReader;
import com.osbblevymista.models.AdminInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Getter
public class AdminProcessor {

    private static AdminProcessor adminProcessor;
    private Date createDate = new Date();

    private final List<AdminInfo> adminInfos;

    private final long REFRESH_TIME_OUT = 1000;

    private AdminFileReader adminFileReader;

    public static AdminProcessor createInstance() throws IOException {
        if (adminProcessor == null || adminProcessor.refresh()){
            adminProcessor = new AdminProcessor();
        }

        return adminProcessor;
    }

    private AdminProcessor() throws IOException {
        adminFileReader = new AdminFileReader();
        adminInfos = adminFileReader.get();
    }

    public void addAdmin(String firstName, String lastName, long userId) throws IOException, CsvException {
        adminFileReader.add(firstName, lastName, userId);
    }

    public boolean isAdmin(long adminId){
        return adminInfos.stream().anyMatch(item -> {
            return item.getAdminId().equals(adminId) && item.isActive();
        });
    }

    private boolean refresh(){

        if ((createDate.getTime() + REFRESH_TIME_OUT) <= new Date().getTime()){
            createDate = new Date();
            return true;
        } else {
            return false;
        }

    }

}
