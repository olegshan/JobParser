package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.parser.Parser;
import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static java.lang.Integer.parseInt;

public class JobParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(Parser.class);

    protected JobSite jobSite;

    public JobParser(JobSite jobSite) {
        this.jobSite = jobSite;
    }

    public Document getDoc(String siteUrl) throws ParserException {
        Document doc;
        try {
            doc = Jsoup.connect(siteUrl).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            LOGGER.error("Connecting to {} failed", siteUrl);
            throw new ParserException("Failed connecting to " + siteUrl + "\n" + e.getMessage());
        }
        return doc;
    }

    public Elements getJobBlocks(Document doc) throws ParserException {
        Elements jobBlocks = doc.getElementsByAttributeValue(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
        check(jobBlocks, "job blocks", null);
        return jobBlocks;
    }

    public Elements getTitleBlock(Element job) throws ParserException {
        Elements titleBlock = job.getElementsByAttributeValue(jobSite.getTitleBox()[0], jobSite.getTitleBox()[1]);
        check(titleBlock, "title blocks", null);
        return titleBlock;
    }

    public String getTitle(Elements titleBlock) {
        return titleBlock.text();
    }

    public String getDescription(Element job) {
        String[] descriptionData = jobSite.getDescriptionData();
        String description = job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
        return description;
    }

    public String getCompany(Element job, String url) throws ParserException {
        String company = job.getElementsByAttributeValue(jobSite.getCompanyData()[0], jobSite.getCompanyData()[1]).text();
        check(company, "company", url);
        return company;
    }

    public LocalDateTime getDate(Element job, String url) throws ParserException {
        String dateLine = job.getElementsByAttributeValue(jobSite.getDateData()[0],
                jobSite.getDateData()[1]).text();
        check(dateLine, "date", url);
        return getDateByLine(job.getElementsByAttributeValue(jobSite.getDateData()[0],
                jobSite.getDateData()[1]).text());
    }

    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);
        return LocalDate.of(parseInt(dateParts[2]), parseInt(dateParts[1]), parseInt(dateParts[0])).atTime(getTime());
    }

    protected LocalTime getTime() {
        return LocalTime.now(ZoneId.of("Europe/Athens"));
    }

    protected void check(Object o, String data, String url) throws ParserException {
        String jobUrl = url == null ? "" : url;
        if (o == null || o.toString().length() == 0) {
            LOGGER.error("Error getting {} from {}, {}", data, jobSite.getSiteName(), jobUrl);
            throw new ParserException("Error getting " + data + " from " + jobSite.getSiteName() + "\n" + jobUrl);
        }
    }
}
