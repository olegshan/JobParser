package com.olegshan.service;

import com.olegshan.entity.Job;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by olegshan on 26.09.2016.
 */
public class DouService {

    private static Document doc = null;

    public static List<Job> douJobs() {

        List<Job> jobs = new ArrayList<>();

        try {
            doc = Jsoup.connect("https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java").get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements jobBlocks = doc.getElementsByAttributeValue("class", "vacancy");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByAttributeValue("class", "vt");
            String url = titleBlock.attr("href");
            String title = titleBlock.text();
            String company = job.getElementsByAttributeValue("class", "company").text();
            String description = job.getElementsByAttributeValue("class", "sh-info").text();
            String source = "dou.ua";
            Date date = getDateForDou(url);

            Job douJob = new Job(title, description, company, source, url, date);
            jobs.add(douJob);
        });
        jobs.forEach(System.out::println);
        return jobs;
    }

    private static Date getDateForDou(String url) {
        Document dateDoc = null;
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

        dateLine = dateDoc.getElementsByAttributeValue("class", "date").text();
        dateParts = dateLine.split(" ");
        day = Integer.parseInt(dateParts[0]);
        year = Integer.parseInt(dateParts[2]);

        month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());

        return new Date(year, month, day);
    }

    public static void main(String[] args) {
        douJobs();
    }
}
