package com.olegshan.controllers;

import com.olegshan.entity.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by olegshan on 24.09.2016.
 */
@RestController
public class ParseController {

    private static List<Job> jobs = new ArrayList<>();

    private Document doc = null;


                                                    // Rabota.ua


    @RequestMapping("/rabota")
    public String findJobAtRabota() {

        try {
            doc = Jsoup.connect("http://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements jobBlocks = doc.getElementsByAttributeValue("class", "v");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByAttributeValue("class", "t");
            String url = "http://rabota.ua/" + titleBlock.attr("href");
            String title = titleBlock.text();
            String company = job.getElementsByAttributeValue("class", "rua-p-c-default").text();
            String description = job.getElementsByAttributeValue("class", "d").text();
            String source = "rabota.ua";
            Date date = getDateForRabotaUa(url);

            Job rabotaJob = new Job(title, description, company, source, url, date);
            jobs.add(rabotaJob);
        });

        jobs.forEach(System.out::println);

        return "Rabota.ua done";
    }

    //date on rabota.ua isn't on search result page, so we need to go inside of each vacancy page
    private Date getDateForRabotaUa(String url) {
        Document dateDoc = null;
        Date date = null;
        String dateLine = "";
        String[] dateParts;
        int year;
        int month;
        int day;

        try {
            dateDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*
        * There are several problems here.
        * First: there are two types of date tags, used on the site on different pages: "d-date" and "datePosted".
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd. Hrrrrrr.
        */

        Elements dateElement = dateDoc.getElementsByAttributeValue("id", "d-date");
        if (dateElement.isEmpty()) {
            dateLine = dateDoc.getElementsByAttributeValue("itemprop", "datePosted").text();
        } else {
            for (Element e : dateElement) {
                dateLine = e.getElementsByAttributeValue("class", "d-ph-value").text();
            }
        }

        try {
            dateParts = dateLine.split("\\.");
            if (dateParts[1].startsWith("0")) {
                dateParts[1] = dateParts[1].substring(1);
            }
            year = Integer.parseInt(dateParts[2]);
            month = Integer.parseInt(dateParts[1]) - 1;
            day = Integer.parseInt(dateParts[0]);
        } catch (ArrayIndexOutOfBoundsException e) {
            dateParts = dateLine.split("-");
            if (dateParts[1].startsWith("0")) {
                dateParts[1] = dateParts[1].substring(1);
            }
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]) - 1;
            day = Integer.parseInt(dateParts[2]);
        }

        //temporary, this will be replaced by something not deprecated
        date = new Date(year, month, day);

        return date;
    }


                                            // Work.ua


    @RequestMapping("/work")
    public String findJobAtWork() {

        try {
            doc = Jsoup.connect("https://www.work.ua/jobs-kyiv-java/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements jobBlocks = doc.getElementsByAttributeValueStarting("class", "card card-hover card-visited job-link");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByTag("a");
            String url = "https://work.ua/" + titleBlock.attr("href");
            String title = titleBlock.text();
            String company = getCompanyNameForWorkUa(url);
            String description = job.getElementsByAttributeValue("class", "text-muted overflow").text();
            String source = "work.ua";
            Date date = getDateForWorkUa(titleBlock.attr("title"));

            Job workJob = new Job(title, description, company, source, url, date);
            jobs.add(workJob);
        });

        jobs.forEach(System.out::println);

        return "Work.ua done";
    }

    private Date getDateForWorkUa(String line) {
        String dateLine[] = line.substring(line.length() - 8).split("\\.");
        int day = Integer.parseInt(dateLine[0]);
        if (dateLine[1].startsWith("0")) {
            dateLine[1] = dateLine[1].substring(1);
        }
        int month = Integer.parseInt(dateLine[1]) - 1;
        int year = 2000 + Integer.parseInt(dateLine[2]);

        return new Date(year, month, day);
    }

    private String getCompanyNameForWorkUa(String url) {
        Document companyDoc = null;
        String company = "";

        try {
            companyDoc = Jsoup.connect(url).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements companyBlock = companyDoc.getElementsByAttributeValue("class", "dl-horizontal");

        for (Element e : companyBlock) {
            company = e.getElementsByTag("a").text();
        }
        return company;
    }
}