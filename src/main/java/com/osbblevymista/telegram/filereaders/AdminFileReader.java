package com.osbblevymista.telegram.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.models.AdminInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AdminFileReader extends FileReader<AdminInfo> {

    private AdminInfo adminInfo;

    public boolean add(String firstName, String lastName, Long adminId) throws IOException, CsvException {
        this.adminInfo = new AdminInfo();
        adminInfo.setFirstName(firstName);
        adminInfo.setLastName(lastName);
        adminInfo.setAdminId(adminId);
        return add();
    }

    @Override
    public String getFileName(){
        return "adminInfo.csv";
    }

    @Override
    protected boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
        List<AdminInfo> userInfoList = getAll();

        if (noneMatch(userInfoList, this.adminInfo)) {
            super.writeToFile(adminInfo);
            return true;
        }
        return false;
    }

    @Override
    public List<AdminInfo> getAll(boolean force) throws IOException {
        return readFromFile(AdminInfo.class);
    }

    @Override
    protected boolean noneMatch(List<AdminInfo> userInfoList, AdminInfo uInfo) {
        return userInfoList.stream().noneMatch(item -> match(item, uInfo));
    }

    @Override
    protected boolean match(AdminInfo el1, AdminInfo el2) {
        return el1.getAdminId().equals(el2.getAdminId());
    }

}
