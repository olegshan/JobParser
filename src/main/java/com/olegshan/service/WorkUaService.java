package com.olegshan.service;

import com.olegshan.entity.Job;
import com.olegshan.tools.MonthsTools;
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
public class WorkUaService {

    public static List<Job> getJobs() throws IOException {

        List<Job> jobs = new ArrayList<>();


        Document doc = Jsoup.connect("https://www.work.ua/jobs-kyiv-java/").get();

        Elements jobBlocks = doc.getElementsByAttributeValueStarting("class", "card card-hover card-visited job-link");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByTag("a");
            String url = "https://work.ua" + titleBlock.attr("href");
            String title = titleBlock.text();
            String company = getCompanyNameForWorkUa(url);
            String description = job.getElementsByAttributeValue("class", "text-muted overflow").text();
            String source = "work.ua";
            Date date = getDateForWorkUa(titleBlock.attr("title"));

            Job workJob = new Job(title, description, company, source, url, date);
            jobs.add(workJob);
        });

        jobs.forEach(System.out::println);

        return jobs;
    }

    private static Date getDateForWorkUa(String line) {
        String dateLine[] = line.substring(line.length() - 8).split("\\.");
        MonthsTools.removeZero(dateLine);
        int day = Integer.parseInt(dateLine[0]);
        int month = Integer.parseInt(dateLine[1]) - 1;
        int year = 2000 + Integer.parseInt(dateLine[2]);

        return new Date(year, month, day);
    }

    private static String getCompanyNameForWorkUa(String url) {
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

    public static void main(String[] args) throws IOException {
        getJobs();
    }
}
