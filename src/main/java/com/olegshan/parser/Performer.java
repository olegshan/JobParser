package com.olegshan.parser;

import com.olegshan.sites.JobSite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by olegshan on 06.10.2016.
 */
@Component
public class Performer {
    private List<JobSite> sites;
    private Parser parser;

    @Autowired
    public Performer(List<JobSite> sites, Parser parser) {
        this.sites = sites;
        this.parser = parser;
    }

    @Scheduled(cron = "0 1 7-23 * * *", zone = "Europe/Athens")
    public void perform() {
        for (JobSite jobSite : sites)
            parser.parse(jobSite);
    }
}
