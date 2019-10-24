package com.olegshan.parser.siteparsers;

import com.olegshan.exception.ParserException;
import com.olegshan.sites.JobSite;
import com.olegshan.util.TimeUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

public class DouUaJobParser extends JobParser {

    public DouUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public LocalDateTime getDate(Element job, String url) throws ParserException {

        Document dateDoc = getDoc(url);

        String dateLine = getElements(dateDoc, jobSite.date()).text();
        check(dateLine, "date line", url);
        String[] dateParts = dateLine.split(jobSite.split());
        TimeUtil.removeZero(dateParts);

        int year = parseInt(dateParts[2]);
        int month = TimeUtil.MONTHS.get(dateParts[1].toLowerCase());
        int day = parseInt(dateParts[0]);

        return LocalDate.of(year, month, day).atTime(getTime());
    }
}
