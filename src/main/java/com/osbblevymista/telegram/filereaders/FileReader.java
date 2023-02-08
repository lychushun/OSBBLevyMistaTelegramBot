package com.osbblevymista.telegram.filereaders;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.telegram.models.Info;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public abstract class FileReader<R extends Info> {

    protected final Logger logger = LoggerFactory.getLogger(FileReader.class);

    @Getter
    @Value("${storagefiles}")
    protected String storageLocation;

    public abstract String getFileName();

    public String getFullFileName(){
        String fullFileName = storageLocation + getFileName();
        logger.info("Getting full file name: " + fullFileName);
        return fullFileName;
    }

//    protected abstract boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException;

    public abstract List<R> getAll(boolean force) throws IOException;


    public void add(R file) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
            writeToFile(file);
    }


    public boolean contains(R el) throws IOException{
        return contains(el, false);
    }

    public boolean contains(R el, boolean getFlash) throws IOException{
        List<R> userInfoList = getAll(getFlash);
        boolean res = !noneMatch(userInfoList, el);
        return res;
    }

    protected abstract boolean noneMatch(List<R> userInfoList, R uInfo);

    protected abstract boolean match(R el1, R el2);

    public List<R> getAll() throws IOException {
        return getAll(false);
    }

    public List<R> get(R el) throws IOException {
        List<R> list = getAll();
        return list.stream().filter(item -> {
            return match(item, el);
        }).collect(Collectors.toList());
    }

    public void deleteFile(){
        logger.info("Deleting file...");
        File f= new File(getFullFileName());           //file to be delete
        f.delete();
    }

    protected List<R> readFromFile(Class<R> c) throws IOException {
        logger.info("Reading file...");
        String fullFileName = getFullFileName();
        File file = new File(fullFileName);
        List<R> list = new ArrayList<>();
        if (file.exists()) {
            Reader reader = Files.newBufferedReader(Path.of(fullFileName));
            CsvToBean<R> csvToBean = new CsvToBeanBuilder<R>(reader)
                    .withType(c)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            list = csvToBean.parse();
            return list;
        }
//        else {
//            file.createNewFile();
//        }
        return list;
    }

    protected void writeToFile(R info) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {
        logger.info("Writing file...");
        String fullFileName = getFullFileName();
        File file = new File(fullFileName);
        if (!file.exists()){
            file.createNewFile();
        }
        CSVReader reader = new CSVReader(new java.io.FileReader(fullFileName));
        String[] nextLine = reader.readNext();
        reader.close();

        List<R> list = new ArrayList<>();
        list.add(info);

        Writer writer = new FileWriter(fullFileName, true);
        CSVWriter csvWriter = new CSVWriter(writer);

        if (nextLine != null) {
            csvWriter.writeNext(info.getAsArray());
        } else {
            StatefulBeanToCsvBuilder<R> builder = new StatefulBeanToCsvBuilder<>(csvWriter);
            StatefulBeanToCsv<R> beanWriter = builder.build();
            beanWriter.write(list);
        }

        csvWriter.close();
        writer.close();
    }


}
