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
public class DouService implements JobService {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JobRepository jobRepository;

    public void parse() {
        Document doc = null;
        String docName = this.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);

        try {
            doc = Jsoup.connect("https://jobs.dou.ua/vacancies/?city=%D0%9A%D0%B8%D1%97%D0%B2&category=Java")
                    .timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = doc.getElementsByAttributeValue("class", "vacancy");

        for (Element job : jobBlocks) {
            Elements titleBlock = job.getElementsByAttributeValue("class", "vt");
            String url = titleBlock.attr("href");
            if (jobRepository.findOne(url) != null) {
                continue;
            }
            String title = titleBlock.text();
            String company = job.getElementsByAttributeValue("class", "company").text();
            String description = job.getElementsByAttributeValue("class", "sh-info").text();
            String source = "Dou.ua";
            LocalDate date = getDate(url);

            Job douJob = new Job(title, description, company, source, url, date);

            jobRepository.save(douJob);
        }

        docRepository.save(new Doc(docName, doc.toString()));
    }

    private static LocalDate getDate(String url) {
        Document dateDoc = null;
        String dateLine = "";
        String[] dateParts;
        int year;
        int month;
        int day;

        try {
            dateDoc = Jsoup.connect(url).timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        dateLine = dateDoc.getElementsByAttributeValue("class", "date").text();
        dateParts = dateLine.split(" ");
        MonthsTools.removeZero(dateParts);
        day = Integer.parseInt(dateParts[0]);
        year = Integer.parseInt(dateParts[2]);

        month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());

        return LocalDate.of(year, month, day);
    }
}
