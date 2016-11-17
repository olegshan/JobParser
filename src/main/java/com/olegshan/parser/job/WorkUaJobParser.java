package com.olegshan.parser.job;

import com.olegshan.sites.JobSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class WorkUaJobParser extends JobParser {

    public WorkUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public Elements getJobBlocks(Document doc) {
        return doc.getElementsByAttributeValueStarting(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
    }

    @Override
    public Elements getTitleBlock(Element job) {
        return job.getElementsByTag("a");
    }

    @Override
    public LocalDateTime getDate(Element job, String url, Elements titleBlock) {
        String dateLine = titleBlock.attr("title");
        String[] dateParts = dateLine.substring(dateLine.length() - 8).split(jobSite.getSplit());

        int year = parseInt(dateParts[2] + 2000);
        int month = parseInt(dateParts[1]);
        int day = parseInt(dateParts[0]);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) {
        String[] companyData = jobSite.getCompanyData();
        Document jobDoc = getDoc(url);
        Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);
        return companyBlock.get(0).getElementsByTag("a").text();
    }
}
