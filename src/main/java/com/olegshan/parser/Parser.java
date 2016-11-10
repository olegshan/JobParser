package com.olegshan.parser;

import com.olegshan.entity.Job;
import com.olegshan.service.JobService;
import com.olegshan.sites.*;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

/**
 * Created by olegshan on 03.10.2016.
 */
@Component
public class Parser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);
    @Autowired
    private JobService jobService;

    public void parse(JobSite jobSite) {

        Document doc = getDoc(jobSite.getSiteUrl());
        Elements jobBlocks = getJobBlocks(jobSite, doc);

        for (Element job : jobBlocks) {
            Elements titleBlock = getTitleBlock(jobSite, job);
            String url = jobSite.getUrlPrefix() + titleBlock.attr("href");
            String title = getTitle(titleBlock);
            String description = getDescription(jobSite, job);
            String company = getCompany(jobSite, job, url);
            LocalDateTime date = getDate(jobSite, job, url, titleBlock);

            Job parsedJob = new Job(title, description, company, jobSite.getSiteName(), url, date);
            jobService.save(parsedJob);
        }
        LOGGER.info("Parsing of {} completed", jobSite.getSiteName());
    }

    private Document getDoc(String siteUrl) {
        try {
            return Jsoup.connect(siteUrl).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            LOGGER.error("Connecting to {} failed", siteUrl);
            throw new RuntimeException("Connection failed to " + siteUrl);
        }
    }

    private Elements getJobBlocks(JobSite jobSite, Document doc) {
        String[] jobBox = jobSite.getJobBox();
        if (jobSite instanceof WorkUa) {
            return doc.getElementsByAttributeValueStarting(jobBox[0], jobBox[1]);
        } else if (jobSite instanceof RabotaUa) {
            return getJobBlocksForRabotaUa(doc, jobBox);
        } else {
            return doc.getElementsByAttributeValue(jobBox[0], jobBox[1]);
        }
    }

    private Elements getJobBlocksForRabotaUa(Document doc, String[] jobBox) {
        Elements jobBlocks = new Elements();
        for (int i = 1; i < jobBox.length; i++) {
            Elements jobElements = doc.getElementsByAttributeValue(jobBox[0], jobBox[i]);
            if (jobElements != null && !jobElements.isEmpty()) {
                jobBlocks.addAll(jobElements);
            }
        }
        return jobBlocks;
    }

    private Elements getTitleBlock(JobSite jobSite, Element job) {
        String[] titleBox = jobSite.getTitleBox();
        if (jobSite instanceof WorkUa) {
            return job.getElementsByTag("a");
        } else {
            return job.getElementsByAttributeValue(titleBox[0], titleBox[1]);
        }
    }

    private String getTitle(Elements titleBlock) {
        String title = titleBlock.text();
        if (title.endsWith("Горячая")) {
            title = title.substring(0, title.length() - "Горячая".length());
        }
        return title;
    }

    private String getDescription(JobSite jobSite, Element job) {
        String[] descriptionData = jobSite.getDescriptionData();
        return job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
    }

    private LocalDateTime getDate(JobSite jobSite, Element job, String url, Elements titleBlock) {
        String[] dateData = jobSite.getDateData();
        String dateLine;
        if (jobSite instanceof DouUa) {
            Document dateDoc = getDoc(url);
            dateLine = dateDoc.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            return getDateByLine(jobSite, dateLine);
        } else if (jobSite instanceof RabotaUa) {
            return getDateForRabotaUa(url);
        } else {
            if (jobSite instanceof WorkUa) {
                dateLine = titleBlock.attr("title");
            } else {
                dateLine = job.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            }
            return getDateByLine(jobSite, dateLine);
        }
    }

    private LocalDateTime getDateByLine(JobSite jobSite, String dateLine) {
        String[] dateParts;
        int year;
        int month;
        int day;
        String split = jobSite.getSplit();
        if (jobSite instanceof JobsUa) {
            dateLine = dateLine.substring(0, 10);
        }
        if (jobSite instanceof WorkUa) {
            dateLine = dateLine.substring(dateLine.length() - 8);
        }
        dateParts = dateLine.split(split);
        MonthsTools.removeZero(dateParts);
        day = Integer.parseInt(dateParts[0]);
        if (jobSite instanceof HeadHunterUa) {
            year = LocalDate.now(ZoneId.of("Europe/Athens")).getYear();
        } else year = Integer.parseInt(dateParts[2]);
        if (jobSite instanceof WorkUa) {
            year = year + 2000;
        }
        if (jobSite instanceof DouUa || jobSite instanceof HeadHunterUa) {
            month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        } else month = Integer.parseInt(dateParts[1]);
        return LocalDate.of(year, month, day).atTime(getTime());
    }

    private LocalDateTime getDateForRabotaUa(String url) {
        /*
        * There are several problems here.
        * First: there are two types of date tags, used on rabota.ua on different pages: "d-date" and "datePosted".
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd.
        * Third: sometimes there is no date at all.
        */
        Document dateDoc = getDoc(url);
        String dateLine;
        String[] dateParts;
        int year;
        int month;
        int day;

        Elements dateElements = dateDoc.getElementsByAttributeValue("id", "d-date");
        if (!dateElements.isEmpty()) {
            dateLine = dateElements.get(0).getElementsByAttributeValue("class", "d-ph-value").text();
        } else {
            dateLine = dateDoc.getElementsByAttributeValue("itemprop", "datePosted").text();
            if (dateLine.length() == 0) {
                //no date at all, sometimes it happens
                LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Europe/Athens"));
                LOGGER.debug("There was no date on Rabota.ua, return {}", ldt);
                return ldt;
            }
        }
        try {
            dateParts = dateLine.split("\\.");
            MonthsTools.removeZero(dateParts);
            year = Integer.parseInt(dateParts[2]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[0]);

        } catch (ArrayIndexOutOfBoundsException e) {

            dateParts = dateLine.split("-");
            MonthsTools.removeZero(dateParts);
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[2]);
        }
        return LocalDate.of(year, month, day).atTime(getTime());
    }

    private LocalTime getTime() {
        return LocalTime.now(ZoneId.of("Europe/Athens"));
    }

    private String getCompany(JobSite jobSite, Element job, String url) {
        String[] companyData = jobSite.getCompanyData();
        if (jobSite instanceof JobsUa || jobSite instanceof WorkUa) {
            Document jobDoc = getDoc(url);
            Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);
            return companyBlock.get(0).getElementsByTag("a").first().text();
        } else {
            return job.getElementsByAttributeValue(companyData[0], companyData[1]).text();
        }
    }
}
