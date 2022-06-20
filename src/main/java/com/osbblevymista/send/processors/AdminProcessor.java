package com.osbblevymista.send.processors;

import com.opencsv.exceptions.CsvException;
import com.osbblevymista.filereaders.AdminFileReader;
import com.osbblevymista.models.AdminInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Date;
import java.util.List;

@Component
@Getter
@RequiredArgsConstructor
public class AdminProcessor {

    private Date createDate = new Date();

    private List<AdminInfo> adminInfos;

    @Value("${adminRefreshTime}")
    private String REFRESH_TIME_OUT;

    private final AdminFileReader adminFileReader;

    public void addAdmin(String firstName, String lastName, long userId) throws IOException, CsvException {
        adminFileReader.add(firstName, lastName, userId);
    }

    public boolean isAdmin(long adminId) throws IOException {
        refresh();
        if (adminInfos != null) {
            return adminInfos.stream().anyMatch(item -> {
                return item.getAdminId().equals(adminId) && item.isActive();
            });
        } else {
            return false;
        }
    }

    private void refresh() throws IOException {
        if ((createDate.getTime() + Long.parseLong(REFRESH_TIME_OUT)) <= new Date().getTime()){
            createDate = new Date();
            adminInfos = adminFileReader.get();
        }
    }

}
