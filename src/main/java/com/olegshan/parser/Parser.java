package com.olegshan.parser;

import com.olegshan.entity.Doc;
import com.olegshan.entity.Job;
import com.olegshan.repository.DocRepository;
import com.olegshan.repository.JobRepository;
import com.olegshan.service.*;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Created by olegshan on 03.10.2016.
 */
@Component
public class Parser {

    @Autowired
    private DocRepository docRepository;
    @Autowired
    private JobRepository jobRepository;

    public void parse(JobService jobService,
                      String siteName,
                      String siteToParse,
                      String urlPrefix,
                      String[] jobList,
                      String[] jobBox,
                      String[] titleBox,
                      String[] companyData,
                      String[] descriptionData,
                      String[] dateData) {

        Document doc = getDoc(siteToParse);
        String docName = jobService.getClass().getSimpleName();
        String savedJobs = getSavedJobs(doc, jobList);

        if (isNothingChanged(savedJobs, docName)) {
            return;
        }
        Elements jobBlocks = getJobBlocks(jobService, doc, jobBox);
        for (Element job : jobBlocks) {
            Elements titleBlock = getTitleBlock(jobService, job, titleBox);
            String url = urlPrefix + titleBlock.attr("href");
            if (jobExists(url)) {
                continue;
            }
            String title = getTitle(titleBlock);
            String description = job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
            String company = getCompany(jobService, job, url, companyData);
            LocalDateTime date = getDate(jobService, url, dateData, titleBlock, job);

            Job parsedJob = new Job(title, description, company, siteName, url, date);
            jobRepository.save(parsedJob);
        }
        docRepository.save(new Doc(docName, savedJobs));
    }

    private Document getDoc(String siteToParse) {
        Document doc = null;
        try {
            doc = Jsoup.connect(siteToParse).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return doc;
    }

    private boolean isNothingChanged(String savedJobs, String docName) {
        Doc savedDoc = docRepository.findOne(docName);
        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(savedJobs)) {
                return true;
            }
        }
        return false;
    }

    private boolean jobExists(String url) {
        return jobRepository.findOne(url) != null;
    }

    private String getSavedJobs(Document doc, String[] jobList) {
        return doc.getElementsByAttributeValue(jobList[0], jobList[1]).toString();
    }

    private Elements getJobBlocks(JobService jobService, Document doc, String[] jobBox) {
        Elements jobBlocks;
        if (jobService instanceof WorkUaService) {
            jobBlocks = doc.getElementsByAttributeValueStarting(jobBox[0], jobBox[1]);
        } else if (jobService instanceof RabotaUaService) {
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

    private Elements getTitleBlock(JobService jobService, Element job, String[] titleBox) {
        Elements titleBlock;
        if (jobService instanceof WorkUaService) {
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

    private LocalDateTime getDate(JobService jobService, String url, String[] dateData, Elements titleBlock, Element job) {
        LocalDateTime date;
        String dateLine;
        if (jobService instanceof DouService) {
            Document dateDoc = getDoc(url);
            dateLine = dateDoc.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            date = getDateByLine(jobService, dateLine);
        } else if (jobService instanceof RabotaUaService) {
            date = getDateForRabotaUa(url);
        } else {
            if (jobService instanceof WorkUaService) {
                dateLine = titleBlock.attr("title");
            } else {
                dateLine = job.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            }
            date = getDateByLine(jobService, dateLine);
        }
        return date;
    }

    private LocalDateTime getDateByLine(JobService jobService, String dateLine) {
        String[] dateParts;
        int year;
        int month;
        int day;
        String split = getSplit(jobService);
        if (jobService instanceof JobsUaService) {
            dateLine = dateLine.substring(0, 10);
        }
        if (jobService instanceof WorkUaService) {
            dateLine = dateLine.substring(dateLine.length() - 8);
        }
        dateParts = dateLine.split(split);
        MonthsTools.removeZero(dateParts);
        day = Integer.parseInt(dateParts[0]);
        if (jobService instanceof HeadHunterService) {
            year = LocalDate.now().getYear();
        } else year = Integer.parseInt(dateParts[2]);
        if (jobService instanceof WorkUaService) {
            year = year + 2000;
        }
        if (jobService instanceof DouService || jobService instanceof HeadHunterService) {
            month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        } else month = Integer.parseInt(dateParts[1]);
        return LocalDate.of(year, month, day).atTime(LocalTime.now());
    }

    private LocalDateTime getDateForRabotaUa(String url) {
        /*
        * There are several problems here.
        * First: there are two types of date tags, used on the site on different pages: "d-date" and "datePosted".
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
                return LocalDateTime.now();
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
        return LocalDate.of(year, month, day).atTime(LocalTime.now());
    }

    private String getCompany(JobService jobService, Element job, String url, String[] companyData) {
        String company = "";
        if (jobService instanceof JobsUaService || jobService instanceof WorkUaService) {
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

    private String getSplit(JobService jobService) {
        String split;
        if (jobService instanceof HeadHunterService) {
            split = "\u00a0";
        } else if (jobService instanceof DouService) {
            split = " ";
        } else split = "\\.";
        return split;
    }
}
