package com.olegshan.parser;

import com.olegshan.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 06.10.2016.
 */
@Component
public class Performer {

    @Autowired
    private DouService douService;
    @Autowired
    private RabotaUaService rabotaUaService;
    @Autowired
    private JobsUaService jobsUaService;
    @Autowired
    private WorkUaService workUaService;
    @Autowired
    private HeadHunterService headHunterService;

    @Scheduled(cron = "0 0 7-23 * * *")
    public void perform() {
        douService.parse();
        headHunterService.parse();
        jobsUaService.parse();
        rabotaUaService.parse();
        workUaService.parse();
    }
}
