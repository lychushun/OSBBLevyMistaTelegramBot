package com.osbblevymista.telegram.services;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.filereaders.AdminFileReader;
import com.osbblevymista.telegram.models.AdminInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
@RequiredArgsConstructor
public class AdminInfoService extends FileService<AdminInfo, Long>{

    private Map<Long, AdminInfo> adminInfoMap;
    private final AdminFileReader adminFileReader;

    @Override
    public void addRow(AdminInfo adminInfo) throws CsvRequiredFieldEmptyException, CsvValidationException, CsvDataTypeMismatchException, IOException {
        if (!adminInfoMap.containsKey(adminInfo.getAdminId())){
            adminInfoMap.put(adminInfo.getAdminId(), adminInfo);
            doSnapshot();
        }
    }

    @Override
    public AdminInfo getRow(Long adminId) {
        return adminInfoMap.get(adminId);
    }

    @PostConstruct
    private void initAdminInfo() throws IOException {
        List<AdminInfo> userInfoList = adminFileReader.getAll(true);
        adminInfoMap = userInfoList.stream().collect(Collectors.toMap(AdminInfo::getAdminId, identity()));
    }

    @Override
    public void doSnapshot(){
        adminFileReader.deleteFile();
        adminFileReader.add(new ArrayList<>(adminInfoMap.values()));
    }

    @Override
    public int size(){
        if (adminFileReader != null){
            return adminInfoMap.size();
        }
        return 0;
    }

    @Override
    public Collection<AdminInfo> getAll() {
        return adminInfoMap.values();
    }

    @Override
    public boolean delete(Long id) {
        if (adminInfoMap.containsKey(id)){
            adminInfoMap.remove(id);
            doSnapshot();
            return true;
        }
        return false;
    }

    public boolean isAdmin(long adminId) throws IOException {
        AdminInfo adminInfo = adminInfoMap.get(adminId);
        return adminInfo != null && adminInfo.isActive();
    }

}
