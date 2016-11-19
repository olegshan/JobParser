package com.olegshan.parser.impl;

import com.olegshan.entity.Job;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ParserImpl.class);

    private JobService jobService;

    @Autowired
    public ParserImpl(JobService jobService) {
        this.jobService = jobService;
    }

    public void parse(JobSite jobSite) {

        JobParser jobParser = jobSite.getParser();
        Document doc = jobParser.getDoc(jobSite.getSiteUrl());
        Elements jobBlocks = jobParser.getJobBlocks(doc);

        for (Element job : jobBlocks) {
            Elements titleBlock = jobParser.getTitleBlock(job);
            String url = jobSite.getUrlPrefix() + titleBlock.attr("href");
            String title = jobParser.getTitle(titleBlock);
            String description = jobParser.getDescription(job);
            String company = jobParser.getCompany(job, url);
            LocalDateTime date = jobParser.getDate(job, url, titleBlock);

            Job parsedJob = new Job(title, description, company, jobSite.getSiteName(), url, date);
            jobService.save(parsedJob);
        }
        LOGGER.info("Parsing of {} completed", jobSite.getSiteName());
    }
}
