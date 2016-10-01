package com.olegshan.service;

import com.olegshan.entity.Job;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by olegshan on 28.09.2016.
 */
public class HeadHunterService implements JobService {

    public List<Job> getJobs() {

        List<Job> jobs = new ArrayList<>();

        Document doc = null;
        try {
            doc = Jsoup.connect("https://hh.ua/search/vacancy?text=java&area=115").userAgent("Mozilla")
                    .timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Elements jobBlocks = doc.getElementsByAttributeValue("class", "search-result-description");
        jobBlocks.forEach(job -> {
            Elements titleBlock = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title");
            String url = titleBlock.attr("href");
            String title = titleBlock.text();
            String description = job.
                    getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text();
            String company = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text();
            String source = "HeadHunter";
            String dateLine = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text();
            LocalDate date = getDate(dateLine);

            Job rabotaJob = new Job(title, description, company, source, url, date);
            jobs.add(rabotaJob);

        });
        return jobs;
    }

    private static LocalDate getDate(String line) {
        String[] dateParts;
        int year;
        int month;
        int day;
        dateParts = line.split("\u00a0");
        MonthsTools.removeZero(dateParts);
        day = Integer.parseInt(dateParts[0]);
        year = LocalDate.now().getYear();

        month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());

        return LocalDate.of(year, month, day);
    }
}
