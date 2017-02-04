package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class JobsUaJobParser extends JobParser {

    public JobsUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public Elements getJobBlocks(Document doc) throws ParserException {
        Elements jobBlocks = doc.getElementsByAttributeValue(jobSite.getJobBox()[0], jobSite.getJobBox()[1]);
        check(jobBlocks, "job blocks", null);

        // ad block on jobs.ua has the same tags as the job blocks, so it should be removed
        for (int i = 0; i < jobBlocks.size(); i++) {
            if (jobBlocks.get(i).getElementsByAttributeValue("class", "b-city__title b-city__companies-title")
                    .text().contains("VIP компании в Украине:")) {
                jobBlocks.remove(i);
            }
        }
        return jobBlocks;
    }

    @Override
    public String getDescription(Element job, String url) throws ParserException {
        String[] descriptionData = jobSite.getDescriptionData();
        Document descDoc = getDoc(url);
        String description = descDoc.getElementsByAttributeValue(descriptionData[0], descriptionData[1]).text();
        return description.length() > 250 ? description.substring(0, 250) + ("...") : description;
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        dateLine = dateLine.replaceAll("\u00a0", "").trim();
        String[] dateParts = dateLine.trim().split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);

        int day = parseInt(dateParts[0]);
        int month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        int year = getYear(month);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        String company = job.getElementsByAttributeValue(jobSite.getCompanyData()[0], jobSite.getCompanyData()[1])
                .first().text();
        check(company, "company", url);
        return company;
    }
}
