package com.osbblevymista.telegram.utils;

import com.osbblevymista.telegram.dto.TelegramFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class TelegramFileUtil {

    @Value("${telegram.bot.token}")
    private String token;

    @Value("${telegram.bot.name}")
    private String name;

    private final String telegramURL = "https://api.telegram.org/";

    private final RestTemplate restTemplate;

    public void getFile(String fileId) {
        String filePath = generateGETPathURL(fileId);
        TelegramFileResponse telegramFileResponse = this.restTemplate.getForObject(filePath, TelegramFileResponse.class);
        byte[] imageBytes = restTemplate.getForObject(generateGETFileURL(telegramFileResponse.file_id), byte[].class);
        try {
            Files.write(Paths.get("image.jpg"), imageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateGETPathURL(String fileID) {
        return telegramURL + "bot" + token + "/getFile?file_id=" + fileID;
    }

    private String generateGETFileURL(String filePath) {
        return telegramURL + "bot" + token + "/" + filePath;
    }

}
