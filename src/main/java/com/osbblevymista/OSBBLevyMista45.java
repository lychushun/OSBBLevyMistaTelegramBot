package com.osbblevymista;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OSBBLevyMista45 {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

        try {
            logger.info("Running...");
            SpringApplication.run(OSBBLevyMista45.class, args);
            logger.info("Running api...");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

}

