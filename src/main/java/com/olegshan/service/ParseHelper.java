package com.olegshan.service;

import com.olegshan.entity.Doc;
import com.olegshan.entity.Job;
import com.olegshan.repository.DocRepository;
import com.olegshan.repository.JobRepository;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Created by olegshan on 03.10.2016.
 */
@Component
public class ParseHelper {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JobRepository jobRepository;

    public void parse(JobService jobService,
                      String siteName,
                      String siteToParse,
                      String[] jobBox,
                      String[] titleBox,
                      String urlPrefix,
                      String[] companyData,
                      String[] descriptionData,
                      String[] dateData) {

        String title;
        String description;
        String url;
        String company;
        String source = siteName;
        LocalDate date = null;

        String docName = jobService.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);
        Document doc = getDoc(siteToParse);

        if (savedDoc != null) {
            //if site didn't change since last visit, do nothing
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = getJobBlocks(jobService, doc, jobBox);

        for (Element job : jobBlocks) {

            Elements titleBlock = getTitleBlock(jobService, job, titleBox);
            url = urlPrefix + titleBlock.attr("href");
            title = titleBlock.text();
            description = job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
            company = getCompany(jobService, job, url, companyData);
            date = getDate(jobService, url, dateData, titleBlock, job);

            Job parsedJob = new Job(title, description, company, source, url, date);
            jobRepository.save(parsedJob);
        }
        docRepository.save(new Doc(docName, doc.toString()));
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

    private Elements getJobBlocks(JobService jobService, Document doc, String[] jobBox) {
        Elements jobBlocks = null;
        if (jobService instanceof WorkUaService) {
            jobBlocks = doc.getElementsByAttributeValueStarting(jobBox[0], jobBox[1]);
        } else {
            jobBlocks = doc.getElementsByAttributeValue(jobBox[0], jobBox[1]);
        }
        return jobBlocks;
    }

    private Elements getTitleBlock(JobService jobService, Element job, String[] titleBox) {
        Elements titleBlock = null;
        if (jobService instanceof WorkUaService) {
            titleBlock = job.getElementsByTag("a");
        } else {
            titleBlock = job.getElementsByAttributeValue(titleBox[0], titleBox[1]);
        }
        return titleBlock;
    }

    private LocalDate getDate(JobService jobService, String url, String[] dateData, Elements titleBlock, Element job) {
        LocalDate date = null;
        if (jobService instanceof DouService || jobService instanceof RabotaUaService) {
            date = getDateByUrl(jobService, url, dateData);
        } else {
            String dateLine;
            if (jobService instanceof WorkUaService) {
                dateLine = titleBlock.attr("title");
            } else {
                dateLine = job.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            }
            date = getDateByLine(jobService, dateLine);
        }
        return date;
    }

    private LocalDate getDateByUrl(JobService jobService, String url, String[] dateData) {

        String dateLine = "";
        LocalDate date = null;
        Document dateDoc = getDoc(url);

        if (jobService instanceof DouService) {

            dateLine = dateDoc.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            date = getDateByLine(jobService, dateLine);

        } else if (jobService instanceof RabotaUaService) {
            date = getDateForRabotaUa(dateDoc, dateLine);
        }

        return date;
    }

    private LocalDate getDateByLine(JobService jobService, String dateLine) {

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

        return LocalDate.of(year, month, day);
    }

    private LocalDate getDateForRabotaUa(Document dateDoc, String dateLine) {
        /*
        * RabotaUa madness.
        * There are several problems here.
        * First: there are two types of date tags, used on the site on different pages: "d-date" and "datePosted".
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd. Hrrrrrr.
        * Third: sometimes there is no date at all.
        */

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
                return LocalDate.now();
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

        return LocalDate.of(year, month, day);
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
