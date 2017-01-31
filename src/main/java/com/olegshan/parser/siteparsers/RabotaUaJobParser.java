package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.regex.Pattern;

public class RabotaUaJobParser extends JobParser {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabotaUaJobParser.class);

    public RabotaUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public String getUrl(Elements titleBlock) {
        return jobSite.getUrlPrefix() + titleBlock
                .get(0)
                .getElementsByTag("a")
                .attr("href");
    }

    public Elements getTitleBlock(Element job) throws ParserException {
        Elements titleBlock = job.getElementsByAttributeValueStarting(jobSite.getTitleBox()[0], jobSite.getTitleBox()[1]);
        check(titleBlock, "title blocks", null);
        return titleBlock;
    }

    @Override
    public String getDescription(Element job, String url) throws ParserException {
        String[] descriptionData = jobSite.getDescriptionData();
        return job.getElementsByAttributeValueStarting(descriptionData[0], descriptionData[1]).text();
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        String company = job.getElementsByAttributeValueStarting(jobSite.getCompanyData()[0], jobSite.getCompanyData()[1]).text();
        if (company.length() == 0) {
            company = "Анонимный работодатель";
        }
        return company;
    }

    public String getDescription(Element job) {
        String[] descriptionData = jobSite.getDescriptionData();
        return job.getElementsByAttributeValueStarting(descriptionData[0], descriptionData[1]).text();
    }

    @Override
    public LocalDateTime getDate(Element job, String url) throws ParserException {
        /*
        * There are several problems here.
        * First: there are different types of date tags, used on rabota.ua on different pages
        * Second: sometimes date format is dd.mm.yyyy and sometimes — yyyy-mm-dd.
        * Third: sometimes there is no date at all.
        */
        Document dateDoc = getDoc(url);
        String dateLine;
        String[] dateParts;
        int year;
        int month;
        int day;

        Elements dateElements = dateDoc.getElementsByAttributeValue("id", "d-date");
        if (!dateElements.isEmpty()) {
            dateLine = dateElements.get(0).getElementsByAttributeValue("class", "d-ph-value").text();
        } else {
            dateLine = dateDoc.getElementsByAttributeValue("itemprop", "datePosted").text();
            if (dateLine.length() == 0) {
                dateLine = dateDoc.getElementsByAttributeValueStarting("class", "f-date-holder").text();
            }
            if (dateLine.length() == 0) {
                //no date at all, sometimes it happens
                LocalDateTime ldt = LocalDateTime.now(ZoneId.of("Europe/Athens"));
                LOGGER.warn("There was no date on Rabota.ua, return {}", ldt);
                return ldt;
            }
        }

        if (Pattern.matches("\\d{2}\\.\\d{2}\\.\\d{4}", dateLine)) {
            dateParts = dateLine.split("\\.");
            MonthsTools.removeZero(dateParts);
            year = Integer.parseInt(dateParts[2]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[0]);
        } else {
            //for format yyyy-mm-dd
            dateParts = dateLine.split("-");
            MonthsTools.removeZero(dateParts);
            year = Integer.parseInt(dateParts[0]);
            month = Integer.parseInt(dateParts[1]);
            day = Integer.parseInt(dateParts[2]);
        }
        return LocalDate.of(year, month, day).atTime(getTime());
    }
}
