package com.olegshan.parser;

import com.olegshan.sites.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by olegshan on 06.10.2016.
 */
@Component
public class Performer {

    @Autowired
    private DouUa douUa;
    @Autowired
    private RabotaUa rabotaUa;
    @Autowired
    private JobsUa jobsUa;
    @Autowired
    private WorkUa workUa;
    @Autowired
    private HeadHunterUa headHunterUa;

    @Scheduled(cron = "0 0/1 * * * *")
//    @Scheduled(cron = "0 0 7-23 * * *")
    public void perform() {
        douUa.parse();
        headHunterUa.parse();
        jobsUa.parse();
        rabotaUa.parse();
        workUa.parse();
    }
}
