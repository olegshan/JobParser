package com.olegshan.parser.job;

import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.lang.Integer.parseInt;

/**
 * @author Taras Zubrei
 */
public class DouUaJobParser extends JobParser {
    public DouUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    public LocalDateTime getDate(Element job, Document doc, Elements titleBlock) {
        return getDateByLine(doc.getElementsByAttributeValue(jobSite.getDateData()[0], jobSite.getDateData()[1]).text());
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);
        return LocalDate.of(parseInt(dateParts[2]), MonthsTools.MONTHS.get(dateParts[1].toLowerCase()), parseInt(dateParts[0])).atTime(getTime());
    }
}
