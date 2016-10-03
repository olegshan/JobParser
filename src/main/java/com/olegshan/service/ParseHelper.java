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

        Document doc = null;
        String docName = jobService.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);

        try {
            doc = Jsoup.connect(siteToParse).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = null;

        if (jobService instanceof WorkUaService) {
            jobBlocks = doc.getElementsByAttributeValueStarting(jobBox[0], jobBox[1]);
        } else {
            jobBlocks = doc.getElementsByAttributeValue(jobBox[0], jobBox[1]);
        }

        for (Element job : jobBlocks) {
            Elements titleBlock = null;
            if (jobService instanceof WorkUaService) {
                titleBlock = job.getElementsByTag("a");
            } else {
                titleBlock = job.getElementsByAttributeValue(titleBox[0], titleBox[1]);
            }
            url = urlPrefix + titleBlock.attr("href");
            title = titleBlock.text();
            description = job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();

            if (jobService instanceof JobsUaService || jobService instanceof WorkUaService) {
                company = getCompany(url, companyData);
            } else {
                company = job.getElementsByAttributeValue(companyData[0], companyData[1]).text();
            }

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

            Job parsedJob = new Job(title, description, company, source, url, date);

            jobRepository.save(parsedJob);
        }

        docRepository.save(new Doc(docName, doc.toString()));
    }


    private LocalDate getDateByUrl(JobService jobService, String url, String[] dateData) {

        Document dateDoc = null;
        String dateLine = "";
        LocalDate date = null;

        try {
            dateDoc = Jsoup.connect(url).timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (jobService instanceof DouService) {

            dateLine = dateDoc.getElementsByAttributeValue(dateData[0], dateData[1]).text();
            date = getDateByLine(jobService, dateLine);

        } else if (jobService instanceof RabotaUaService) {

        /*
        * RabotaUa madness
        * There are several problems here.
        * First: there are two types of date tags, used on the site on different pages: "d-date" and "datePosted".
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd. Hrrrrrr.
        * Third: sometimes there is no date at all.
        */

            String[] dateParts;
            int year;
            int month;
            int day;

            Elements dateElement = dateDoc.getElementsByAttributeValue("id", "d-date");
            if (!dateElement.isEmpty()) {
                for (Element e : dateElement) {
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

            date = LocalDate.of(year, month, day);
        }

        return date;
    }

    private LocalDate getDateByLine(JobService jobService, String dateLine) {

        String[] dateParts;
        int year;
        int month;
        int day;
        String split;

        if (jobService instanceof HeadHunterService) {
            split = "\u00a0";
        } else if (jobService instanceof DouService) {
            split = " ";
        } else split = "\\.";

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

    private String getCompany(String url, String companyData[]) {
        Document jobDoc = null;
        String company = "";
        try {
            jobDoc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);

        for (Element e : companyBlock) {
            company = e.getElementsByTag("a").first().text();
        }
        return company;
    }
}
