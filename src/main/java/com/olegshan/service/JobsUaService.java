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
 * Created by olegshan on 27.09.2016.
 */
public class JobsUaService {

    public static List<Job> jobsUaJobs() throws IOException {

        List<Job> jobs = new ArrayList<>();

        Document doc = Jsoup.connect("http://www.jobs.ua/vacancy/rabota-kiev-java/").get();

        Elements jobBlocks = doc.getElementsByAttributeValue("class", "div_vac_list");

        jobBlocks.forEach(job -> {
            /*System.out.println(job);
            System.out.println();
            System.out.println();*/
            Elements titleBlock = job.getElementsByAttributeValue("class", "jvac_view");
            String url = "http://www.jobs.ua" + titleBlock.attr("href");
            String title = titleBlock.text();
            //can't parse data from vacancy page
            String company = "N/A";
            String description = job.getElementsByAttributeValue("style", "padding-top:12px;").text();
            String source = "jobs.ua";

            String dateLine = job.getElementsByAttributeValue("style", "padding-top:10px").text();
            Date date = getDateForJobsUa(dateLine);

            Job jobsUaJob = new Job(title, description, company, source, url, date);
            jobs.add(jobsUaJob);
        });
        jobs.forEach(System.out::println);
        return jobs;
    }

    private static Date getDateForJobsUa(String dateLine) {
        int day;
        int month;
        int year;

        String[] dateParts = dateLine.substring(0, 10).split("\\.");
        MonthsTools.removeZero(dateParts);

        day = Integer.parseInt(dateParts[0]);
        month = Integer.parseInt(dateParts[1]) - 1;
        year = Integer.parseInt(dateParts[2]);

        return new Date(year, month, day);
    }

    public static void main(String[] args) throws IOException {
        jobsUaJobs();
    }
}
