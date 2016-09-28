package com.olegshan.service;

import com.olegshan.entity.Job;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olegshan on 26.09.2016.
 */
public class WorkUaService implements JobService {

    public List<Job> getJobs() {

        List<Job> jobs = new ArrayList<>();


        Document doc = null;
        try {
            doc = Jsoup.connect("https://www.work.ua/jobs-kyiv-java/").get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements jobBlocks = doc.getElementsByAttributeValueStarting("class", "card card-hover card-visited job-link");

        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByTag("a");
            String url = "https://work.ua" + titleBlock.attr("href");
            String title = titleBlock.text();
            String company = getCompanyName(url);
            String description = job.getElementsByAttributeValue("class", "text-muted overflow").text();
            String source = "Work.ua";
            String dateLine = titleBlock.attr("title");
            LocalDate date = getDate(dateLine);

            Job workJob = new Job(title, description, company, source, url, date);
            jobs.add(workJob);
        });

        jobs.forEach(System.out::println);

        return jobs;
    }

    private static LocalDate getDate(String line) {
        String dateLine[] = line.substring(line.length() - 8).split("\\.");
        MonthsTools.removeZero(dateLine);
        int day = Integer.parseInt(dateLine[0]);
        int month = Integer.parseInt(dateLine[1]);
        int year = 2000 + Integer.parseInt(dateLine[2]);

        return LocalDate.of(year, month, day);
    }

    private static String getCompanyName(String url) {
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
        new WorkUaService().getJobs();
    }
}
