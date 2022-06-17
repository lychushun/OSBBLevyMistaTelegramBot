package com.osbblevymista.filereaders;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.bean.*;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;
import com.osbblevymista.models.Info;
import lombok.Getter;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public abstract class FileReader<R extends Info> {

    @Getter
    private String fileName;
    private Class<R> clazz;

    private FileReader() {
    }

    protected FileReader(Class<R> c, String fileName) {
        this.clazz = c;
        this.fileName = fileName;
    }

    protected abstract boolean add() throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException;

    public abstract List<R> get() throws IOException;


    protected List<R> readFromFile() throws IOException {
        File file = new File(fileName);
        List<R> list = new ArrayList<>();
        if (file.exists()) {
            Reader reader = Files.newBufferedReader(Path.of(fileName));
            CsvToBean<R> csvToBean = new CsvToBeanBuilder<R>(reader)
                    .withType(clazz)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            list = csvToBean.parse();
            return list;
        } else {
            file.createNewFile();
        }
        return list;
    }

    protected void writeToFile(R info) throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException, CsvValidationException {

        CSVReader reader = new CSVReader(new java.io.FileReader(fileName));
        String[] nextLine = reader.readNext();
        reader.close();

        List<R> list = new ArrayList<>();
        list.add(info);

        Writer writer = new FileWriter(fileName, true);
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
