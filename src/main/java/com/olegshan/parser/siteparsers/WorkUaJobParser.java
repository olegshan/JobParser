package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
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
    public Elements getJobBlocks(Document doc) throws ParserException {
        Elements jobBlocks = doc.getElementsByAttributeValueStarting(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
        check(jobBlocks, "job blocks", null);
        return jobBlocks;
    }

    @Override
    public Elements getTitleBlock(Element job) {
        return job.getElementsByTag("a");
    }

    @Override
    public LocalDateTime getDate(Element job, String url) throws ParserException {
        String dateLine = getTitleBlock(job).attr("title");
        String[] dateParts = dateLine.substring(dateLine.length() - 8).split(jobSite.getSplit());
        check(dateParts, "date parts", url);

        int year = parseInt(dateParts[2]) + 2000;
        int month = parseInt(dateParts[1]);
        int day = parseInt(dateParts[0]);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        String[] companyData = jobSite.getCompanyData();
        Document jobDoc = getDoc(url);
        Elements companyBlock = jobDoc.getElementsByAttributeValue(companyData[0], companyData[1]);
        check(companyBlock, "company block", url);
        return companyBlock.get(0).getElementsByTag("a").text();
    }
}
