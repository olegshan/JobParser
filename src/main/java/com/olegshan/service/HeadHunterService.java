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
 * Created by olegshan on 28.09.2016.
 */
@Component
public class HeadHunterService implements JobService {

    @Autowired
    private DocRepository docRepository;

    @Autowired
    private JobRepository jobRepository;

    public void parse() {
        Document doc = null;
        String docName = this.getClass().getSimpleName();
        Doc savedDoc = docRepository.findOne(docName);

        try {
            doc = Jsoup.connect("https://hh.ua/search/vacancy?text=java&area=115").userAgent("Mozilla")
                    .timeout(0).get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (savedDoc != null) {
            if (savedDoc.getDoc().equals(doc.toString())) {
                return;
            }
        }

        Elements jobBlocks = doc.getElementsByAttributeValue("class", "search-result-description");

        for (Element job : jobBlocks) {
            Elements titleBlock = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-title");
            String url = titleBlock.attr("href");
            if (jobRepository.findOne(url) != null) {
                continue;
            }
            String title = titleBlock.text();
            String description = job.
                    getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy_snippet_requirement").text();
            String company = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-employer").text();
            String source = "HeadHunter";
            String dateLine = job.getElementsByAttributeValue("data-qa", "vacancy-serp__vacancy-date").text();
            LocalDate date = getDate(dateLine);

            Job headHunterJob = new Job(title, description, company, source, url, date);
            jobRepository.save(headHunterJob);
        }

        docRepository.save(new Doc(docName, doc.toString()));
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
