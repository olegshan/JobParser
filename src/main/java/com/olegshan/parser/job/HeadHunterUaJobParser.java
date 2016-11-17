package com.olegshan.parser.job;

import com.olegshan.sites.JobSite;
import com.olegshan.tools.MonthsTools;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import static java.lang.Integer.parseInt;

public class HeadHunterUaJobParser extends JobParser {

    public HeadHunterUaJobParser(JobSite jobSite) {
        super(jobSite);
    }

    @Override
    protected LocalDateTime getDateByLine(String dateLine) {
        String[] dateParts = dateLine.split(jobSite.getSplit());
        MonthsTools.removeZero(dateParts);

        int year = LocalDate.now(ZoneId.of("Europe/Athens")).getYear();
        int month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        int day = parseInt(dateParts[0]);

        return LocalDate.of(year, month, day).atTime(getTime());
    }
}
