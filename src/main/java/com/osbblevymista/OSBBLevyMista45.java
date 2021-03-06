package com.osbblevymista;

import com.osbblevymista.send.SendMessageBuilder;
import com.osbblevymista.send.processors.ActionSendMessageProcessor;
import com.osbblevymista.send.processors.SessionSendMessageProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OSBBLevyMista45 {
    public static void main(String[] args) {
        final Logger logger = LoggerFactory.getLogger(OSBBLevyMista45.class);

        try {
            logger.info("Running...");
            SpringApplication.run(OSBBLevyMista45.class, args);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
        }
    }

}

