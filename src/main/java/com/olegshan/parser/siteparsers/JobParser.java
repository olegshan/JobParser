package com.olegshan.parser.siteparsers;

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

    public Document getDoc(String siteUrl) {
        try {
            return Jsoup.connect(siteUrl).userAgent("Mozilla").timeout(0).get();
        } catch (IOException e) {
            LOGGER.error("Connecting to {} failed", siteUrl);
            throw new RuntimeException("Connection failed to " + siteUrl);
        }
    }

    public Elements getJobBlocks(Document doc) {
        return doc.getElementsByAttributeValue(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
    }

    public Elements getTitleBlock(Element job) {
        return job.getElementsByAttributeValue(jobSite.getTitleBox()[0], jobSite.getTitleBox()[1]);
    }

    public String getTitle(Elements titleBlock) {
        return titleBlock.text();
    }

    public String getDescription(Element job) {
        String[] descriptionData = jobSite.getDescriptionData();
        return job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
    }

    public String getCompany(Element job, String url) {
        return job.getElementsByAttributeValue(jobSite.getCompanyData()[0], jobSite.getCompanyData()[1]).text();
    }

    public LocalDateTime getDate(Element job, String url, Elements titleBlock) {
        return getDateByLine(job.getElementsByAttributeValue(jobSite.getDateData()[0], jobSite.getDateData()[1]).text());
    }

    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);
        return LocalDate.of(parseInt(dateParts[2]), parseInt(dateParts[1]), parseInt(dateParts[0])).atTime(getTime());
    }

    protected LocalTime getTime() {
        return LocalTime.now(ZoneId.of("Europe/Athens"));
    }
}
