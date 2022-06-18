package com.osbblevymista.filereaders;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.models.AdminInfo;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class AdminFileReader extends FileReader<AdminInfo> {

    private AdminInfo adminInfo;

//    public AdminFileReader(){
//        super(AdminInfo.class, "adminInfo.csv");
//    }

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
        List<AdminInfo> userInfoList = get();

        if (userInfoList.stream().noneMatch(item -> item.getAdminId().equals(this.adminInfo.getAdminId()))) {
            super.writeToFile(adminInfo);
            return true;
        }
        return false;
    }

    @Override
    public List<AdminInfo> get() throws IOException {
        return readFromFile(AdminInfo.class);
    }
}
