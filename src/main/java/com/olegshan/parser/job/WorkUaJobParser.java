package com.olegshan.parser.job;

import com.olegshan.sites.JobSite;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

/**
 * @author Taras Zubrei
 */
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
    public LocalDateTime getDate(Element job, Document doc, Elements titleBlock) {
        String dateLine = titleBlock.attr("title");
        return getDateByLine(dateLine);
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.substring(dateLine.length() - 8).split(jobSite.getSplit());
        return LocalDate.of(parseInt(dateParts[2]) + 2000, parseInt(dateParts[1]), parseInt(dateParts[0])).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, Document doc) {
        String[] companyData = jobSite.getCompanyData();
        Elements companyBlock = doc.getElementsByAttributeValue(companyData[0], companyData[1]);
        return companyBlock.get(0).getElementsByTag("a").first().text();

    }
}
