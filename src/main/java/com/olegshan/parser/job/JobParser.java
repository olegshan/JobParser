package com.olegshan.parser.job;

import com.olegshan.sites.*;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

import static java.lang.Integer.*;

/**
 * @author Taras Zubrei
 */
public class JobParser {
    protected JobSite jobSite;

    public JobParser(JobSite jobSite) {
        this.jobSite = jobSite;
    }

    public Elements getJobBlocks(Document doc) {
        return doc.getElementsByAttributeValue(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
    }

    public Elements getTitleBlock(Element job) {
        return job.getElementsByAttributeValue(jobSite.getTitleBox()[0], jobSite.getTitleBox()[1]);
    }

    public String getCompany(Element job, Document doc) {
        return job.getElementsByAttributeValue(jobSite.getCompanyData()[0], jobSite.getCompanyData()[1]).text();
    }
    public String getTitle(Elements titleBlock) {
        String title = titleBlock.text();
        if (title.endsWith("Горячая")) {
            title = title.substring(0, title.length() - "Горячая".length());
        }
        return title;
    }

    public String getDescription(Element job) {
        String[] descriptionData = jobSite.getDescriptionData();
        return job.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
    }

    public LocalDateTime getDate(Element job, Document doc, Elements titleBlock) {
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
