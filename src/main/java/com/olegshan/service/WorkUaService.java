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
 * Created by olegshan on 26.09.2016.
 */
@Component
public class WorkUaService implements JobService {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JobRepository jobRepository;

    public void parse() {
        Document doc = null;
        String docName = this.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);

        try {
            doc = Jsoup.connect("https://www.work.ua/jobs-kyiv-java/").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = doc.getElementsByAttributeValueStarting("class", "card card-hover card-visited job-link");

        for (Element job : jobBlocks) {
            Elements titleBlock = job.getElementsByTag("a");
            String url = "https://work.ua" + titleBlock.attr("href");
            if (jobRepository.findOne(url) != null) {
                continue;
            }
            String title = titleBlock.text();
            String company = getCompanyName(url);
            String description = job.getElementsByAttributeValue("class", "text-muted overflow").text();
            String source = "Work.ua";
            String dateLine = titleBlock.attr("title");
            LocalDate date = getDate(dateLine);

            Job workJob = new Job(title, description, company, source, url, date);
            jobRepository.save(workJob);
        }
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
            companyDoc = Jsoup.connect(url).timeout(0).get();
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
