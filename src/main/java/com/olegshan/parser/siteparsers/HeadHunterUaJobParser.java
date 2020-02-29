package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class HeadHunterUaJobParser extends JobParser {

    public HeadHunterUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.split());
        TimeUtil.removeZero(dateParts);

        int day = parseInt(dateParts[0]);
        int month = TimeUtil.MONTHS.get(dateParts[1].toLowerCase());
        int year = getYear(month);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    @Override
    public String getCompany(Element job, String url) throws ParserException {
        Document innerJob = getDoc(url);
        return super.getCompany(innerJob, url);
    }
}
