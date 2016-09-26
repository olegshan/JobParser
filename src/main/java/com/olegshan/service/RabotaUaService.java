package com.olegshan.service;

import com.olegshan.entity.Job;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by olegshan on 26.09.2016.
 */
public class RabotaUaService {

    private static Document doc = null;

    public static List<Job> rabotaUaJobs() {
        List<Job> jobs = new ArrayList<>();
        try {
            doc = Jsoup.connect("http://rabota.ua/zapros/java/%D0%BA%D0%B8%D0%B5%D0%B2").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements jobBlocks = doc.getElementsByAttributeValue("class", "v");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByAttributeValue("class", "t");
            String url = "http://rabota.ua" + titleBlock.attr("href");
            String title = titleBlock.text();
            String company = job.getElementsByAttributeValue("class", "rua-p-c-default").text();
            String description = job.getElementsByAttributeValue("class", "d").text();
            String source = "rabota.ua";
            Date date = getDateForRabotaUa(url);

            Job rabotaJob = new Job(title, description, company, source, url, date);
            jobs.add(rabotaJob);
        });

        jobs.forEach(System.out::println);

        return jobs;
    }

    //date on rabota.ua isn't on search result page, so we need to go inside of each vacancy page
    private static Date getDateForRabotaUa(String url) {
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
        * Third: sometimes there is no date at all.
        */

        Elements dateElement = dateDoc.getElementsByAttributeValue("id", "d-date");
        if (!dateElement.isEmpty()) {
            for (Element e : dateElement) {
                dateLine = e.getElementsByAttributeValue("class", "d-ph-value").text();
            }
        } else {
            dateLine = dateDoc.getElementsByAttributeValue("itemprop", "datePosted").text();
            if (dateLine.length() == 0) {
                //no date at all, sometimes it happens
                return null;
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

    public static void main(String[] args) {
        rabotaUaJobs();
    }

}
