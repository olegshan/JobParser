package com.olegshan.parser.impl;

import com.olegshan.entity.Job;
import com.olegshan.notifier.Notifier;
import com.olegshan.parser.Parser;
import com.olegshan.parser.siteparsers.JobParser;
import com.olegshan.service.JobService;
import com.olegshan.sites.JobSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ParserImpl implements Parser {

    private JobService jobService;
    private Notifier notifier;

    @Autowired
    public ParserImpl(JobService jobService, Notifier notifier) {
        this.jobService = jobService;
        this.notifier = notifier;
    }

    public void parse(JobSite jobSite) {

        JobParser jobParser = jobSite.getParser();
        String url = "";

        try {
            Document doc = jobParser.getDoc(jobSite.getSiteUrl());
            Elements jobBlocks = jobParser.getJobBlocks(doc);

            for (Element job : jobBlocks) {

                Elements titleBlock = jobParser.getTitleBlock(job);
                url = jobParser.getUrl(titleBlock);
                String title = jobParser.getTitle(titleBlock);
                String description = jobParser.getDescription(job, url);
                String company = jobParser.getCompany(job, url);
                LocalDateTime date = jobParser.getDate(job, url);

                Job parsedJob = new Job(title, description, company, jobSite.getSiteName(), url, date);
                jobService.save(parsedJob);
            }
            log.info("Parsing of {} completed\n", jobSite.getSiteName());
        } catch (Exception e) {
            notifier.notifyAdmin("Error while parsing " + url + "\nError message is: " + e.getMessage());
        }
    }

    private static final Logger log = LoggerFactory.getLogger(ParserImpl.class);
}