package com.osbblevymista.telegram.services;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.IOException;
import java.util.Collection;

public abstract class FileService<R, T> {

    public abstract void addRow(R userInfo) throws CsvRequiredFieldEmptyException, CsvValidationException, CsvDataTypeMismatchException, IOException;
    public abstract R getRow(T id);

    public abstract void doSnapshot();
    public abstract int size();
    public abstract Collection<R> getAll();

    public abstract boolean delete(T id);

}
