package com.osbblevymista.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.models.AdminInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AdminFileReader extends FileReader<AdminInfo> {

    @Override
    public String getFileName(){
        return "adminInfo.csv";
    }

    public void add(List<AdminInfo> userInfo) {
        if(userInfo!=null){
            userInfo.forEach(el ->{
                try {
                    super.writeToFile(el);
                } catch (IOException | CsvRequiredFieldEmptyException | CsvDataTypeMismatchException | CsvValidationException e) {
                    e.printStackTrace();
                }
            });
        }
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
