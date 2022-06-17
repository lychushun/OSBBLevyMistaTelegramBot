package com.osbblevymista.extend;

import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class OSBBHeaderColumnNameMappingStrategy<T> extends HeaderColumnNameMappingStrategy<T> {

    private boolean includeHeader = true;


    @Override
    public String[] generateHeader(T bean) throws CsvRequiredFieldEmptyException {
        if (includeHeader) {
            return super.generateHeader(bean);
        } else {
            return new String[0];
        }
    }

}
