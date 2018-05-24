package com.olegshan.parser.siteparsers;

import com.olegshan.sites.JobSite;
import com.olegshan.util.TimeUtil;

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
}
