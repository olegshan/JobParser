package com.olegshan.parser;

import com.olegshan.entity.Job;
import com.olegshan.service.DbService;
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
    private DbService dbService;

    public void parse(JobSite jobSite,
                      String siteName,
                      String siteToParse,
                      String urlPrefix,
                      String[] jobBox,
                      String[] titleBox,
                      String[] companyData,
                      String[] descriptionData,
                      String[] dateData) {

        Document doc = getDoc(siteToParse);
        Elements jobBlocks = getJobBlocks(jobSite, doc, jobBox);

        for (Element job : jobBlocks) {
            Elements titleBlock = getTitleBlock(jobSite, job, titleBox);
            String url = urlPrefix + titleBlock.attr("href");
            String title = getTitle(titleBlock);
            String description = job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
            String company = getCompany(jobSite, job, url, companyData);
            LocalDateTime date = getDate(jobSite, url, dateData, titleBlock, job);

            Job parsedJob = new Job(title, description, company, siteName, url, date);
            dbService.save(parsedJob);
        }
        LOGGER.info("Parsing of {} completed", siteName);
    }

    private Document getDoc(String siteToParse) {
        Document doc;
        try {
            doc = Jsoup.connect(siteToParse).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            LOGGER.error("Connecting to {} failed", siteToParse);
            throw new RuntimeException("Connection failed");
        }
        return doc;
    }

    private Elements getJobBlocks(JobSite jobSite, Document doc, String[] jobBox) {
        Elements jobBlocks;
        if (jobSite instanceof WorkUa) {
            jobBlocks = doc.getElementsByAttributeValueStarting(jobBox[0], jobBox[1]);
        } else if (jobSite instanceof RabotaUa) {
            jobBlocks = getJobBlocksForRabotaUa(doc, jobBox);
        } else {
            jobBlocks = doc.getElementsByAttributeValue(jobBox[0], jobBox[1]);
        }
        return jobBlocks;
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

    private Elements getTitleBlock(JobSite jobSite, Element job, String[] titleBox) {
        Elements titleBlock;
        if (jobSite instanceof WorkUa) {
            titleBlock = job.getElementsByTag("a");
        } else {
            titleBlock = job.getElementsByAttributeValue(titleBox[0], titleBox[1]);
        }
        return titleBlock;
    }

    private String getTitle(Elements titleBlock) {
        String title = titleBlock.text();
        if (title.endsWith("Горячая")) {
            title = title.substring(0, title.length() - "Горячая".length());
        }
        return title;
    }

    private LocalDateTime getDate(JobSite jobSite, String url, String[] dateData, Elements titleBlock, Element job) {
        LocalDateTime date;
        String dateLine;
        if (jobSite instanceof DouUa) {
            Document dateDoc = getDoc(url);
            dateLine = dateDoc.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            date = getDateByLine(jobSite, dateLine);
        } else if (jobSite instanceof RabotaUa) {
            date = getDateForRabotaUa(url);
        } else {
            if (jobSite instanceof WorkUa) {
                dateLine = titleBlock.attr("title");
            } else {
                dateLine = job.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            }
            date = getDateByLine(jobSite, dateLine);
        }
        return date;
    }

    private LocalDateTime getDateByLine(JobSite jobSite, String dateLine) {
        String[] dateParts;
        int year;
        int month;
        int day;
        String split = getSplit(jobSite);
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
            year = LocalDate.now().getYear();
        } else year = Integer.parseInt(dateParts[2]);
        if (jobSite instanceof WorkUa) {
            year = year + 2000;
        }
        if (jobSite instanceof DouUa || jobSite instanceof HeadHunterUa) {
            month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        } else month = Integer.parseInt(dateParts[1]);
        LocalDateTime ldt = LocalDate.of(year, month, day).atTime(getTime());
        LOGGER.info("LocalDateTime: " + ldt);
        return ldt;
    }

    private LocalDateTime getDateForRabotaUa(String url) {
        /*
        * There are several problems here.
        * First: there are two types of date tags, used on the jobSite on different pages: "d-date" and "datePosted".
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd.
        * Third: sometimes there is no date at all.
        */
        Document dateDoc = getDoc(url);
        String dateLine = "";
        String[] dateParts;
        int year;
        int month;
        int day;

        Elements dateElements = dateDoc.getElementsByAttributeValue("id", "d-date");
        if (!dateElements.isEmpty()) {
            for (Element e : dateElements) {
                dateLine = e.getElementsByAttributeValue("class", "d-ph-value").text();
            }
        } else {
            dateLine = dateDoc.getElementsByAttributeValue("itemprop", "datePosted").text();
            if (dateLine.length() == 0) {
                //no date at all, sometimes it happens
                LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Europe/Athens"));
                LOGGER.info("There was no date on Rabota.ua, return {}", ldt);
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
        LocalDateTime ldt = LocalDate.of(year, month, day).atTime(getTime());
        LOGGER.info("LocalDateTime: " + ldt);
        return ldt;
    }

    private LocalTime getTime() {
        return LocalTime.now(ZoneId.of("Europe/Athens"));
    }

    private String getCompany(JobSite jobSite, Element job, String url, String[] companyData) {
        String company = "";
        if (jobSite instanceof JobsUa || jobSite instanceof WorkUa) {
            Document jobDoc = getDoc(url);
            Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);
            for (Element e : companyBlock) {
                company = e.getElementsByTag("a").first().text();
            }
        } else {
            company = job.getElementsByAttributeValue(companyData[0], companyData[1]).text();
        }
        return company;
    }

    private String getSplit(JobSite jobSite) {
        String split;
        if (jobSite instanceof HeadHunterUa) {
            split = "\u00a0";
        } else if (jobSite instanceof DouUa) {
            split = " ";
        } else split = "\\.";
        return split;
    }
}
