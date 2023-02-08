package com.osbblevymista;

import com.osbblevymista.telegram.services.UserInfoService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    private final UserInfoService userInfoService;

    @Scheduled(fixedRate = 50000)
    public void reportCurrentTime() {
        log.info("Creating snapshot of user info {}, size: {}", dateFormat.format(new Date()), userInfoService.size());
        userInfoService.doSnapshot();
    }

}
