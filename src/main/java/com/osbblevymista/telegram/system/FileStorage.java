package com.osbblevymista.telegram.system;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Component
public class FileStorage {

    private final Logger logger = LoggerFactory.getLogger(FileStorage.class);

    private Map<String, String> fileLocation;

    @Value("${temp.dir}")
    private String tempDir;

    {
        fileLocation = new HashMap<>();

    }

    public String addFile(InputStream inputStream, String fileId, String fileName, String fileExt) throws IOException {

        final File tempFile = File.createTempFile(tempDir + fileName, "."+fileExt, new File(tempDir));
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(inputStream, out);
        }

        String absolutePath = tempFile.toString();
        logger.info("Temp file : " + absolutePath);
        fileLocation.put(fileId, absolutePath);
        return absolutePath;
    }

    public String getFilePath(String fileId){
        return fileLocation.get(fileId);
    }

    public void deleteFile(String fileId){
        File file = new File(fileLocation.get(fileId));
        if(file.exists()){
            file.delete();
        }
        fileLocation.remove(fileId);
    }

}
