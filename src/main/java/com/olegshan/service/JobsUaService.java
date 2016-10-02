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
 * Created by olegshan on 27.09.2016.
 */
@Component
public class JobsUaService implements JobService {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JobRepository jobRepository;

    public void parse() {
        Document doc = null;
        String docName = this.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);

        try {
            doc = Jsoup.connect("http://www.jobs.ua/vacancy/rabota-kiev-java/").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = doc.getElementsByAttributeValue("class", "div_vac_list");

        for (Element job : jobBlocks) {
            Elements titleBlock = job.getElementsByAttributeValue("class", "jvac_view");
            String url = "http://www.jobs.ua" + titleBlock.attr("href");
            if (jobRepository.findOne(url) != null) {
                continue;
            }
            String title = titleBlock.text();
            String company = getCompanyName(url);
            String description = job.getElementsByAttributeValue("style", "padding-top:12px;").text();
            String source = "Jobs.ua";

            String dateLine = job.getElementsByAttributeValue("style", "padding-top:10px").text();
            LocalDate date = getDate(dateLine);

            Job jobsUaJob = new Job(title, description, company, source, url, date);
            jobRepository.save(jobsUaJob);
        }

        docRepository.save(new Doc(docName, doc.toString()));
    }

    private static LocalDate getDate(String dateLine) {
        int day;
        int month;
        int year;

        String[] dateParts = dateLine.substring(0, 10).split("\\.");
        MonthsTools.removeZero(dateParts);

        day = Integer.parseInt(dateParts[0]);
        month = Integer.parseInt(dateParts[1]);
        year = Integer.parseInt(dateParts[2]);

        return LocalDate.of(year, month, day);
    }

    private static String getCompanyName(String url) {
        Document jobDoc = null;
        String company = "";
        try {
            jobDoc = Jsoup.connect(url).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements vacancyBlock = jobDoc.getElementsByAttributeValue("class", "viewcontcenter");

        for (Element e : vacancyBlock) {
            company = e.getElementsByTag("a").first().text();
        }
        return company;
    }
}
