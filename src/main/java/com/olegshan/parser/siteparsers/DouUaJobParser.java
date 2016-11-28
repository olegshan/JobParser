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

public class DouUaJobParser extends JobParser {

    public DouUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public LocalDateTime getDate(Element job, String url, Elements titleBlock) throws ParserException {

        Document dateDoc = getDoc(url);

        String dateLine = dateDoc.getElementsByAttributeValue(
                jobSite.getDateData()[0],
                jobSite.getDateData()[1]).text();
        check(dateLine, "date line");
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);

        int year = parseInt(dateParts[2]);
        int month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        int day = parseInt(dateParts[0]);

        return LocalDate.of(year, month, day).atTime(getTime());
    }
}
