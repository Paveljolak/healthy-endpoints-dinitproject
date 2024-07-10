package com.pavel.dinit.project.components;

import com.pavel.dinit.project.services.UrlService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class UrlHealthCheckerScheduler {

    private final UrlService urlService;
    private final Logger logger = LoggerFactory.getLogger(UrlHealthCheckerScheduler.class);


    @Autowired
    public UrlHealthCheckerScheduler(UrlService urlService) {
        this.urlService = urlService;
    }

    @Scheduled(fixedDelayString = "${delayTimeForScheduler}")
    public void checkAllUrlsHealthScheduler(){
        try {
            urlService.checkAllUrlsHealth();
            logger.info("All URLs have been checked.");
        } catch (Exception e) {
            logger.error("There was an error while checking all URLs health.", e);
        }
    }
}