package com.olegshan.parser.siteparsers;

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

        int day = parseInt(dateParts[0]);
        int month = MonthsTools.MONTHS.get(dateParts[1].toLowerCase());
        int year = getYear(month);

        return LocalDate.of(year, month, day).atTime(getTime());
    }

    //in case we parse in January jobs of last December
    private int getYear(int month) {
        int year;
        if (month > LocalDate.now(ZoneId.of("Europe/Athens")).getMonthValue()) {
            year = LocalDate.now().getYear() - 1;
        } else {
            year = LocalDate.now(ZoneId.of("Europe/Athens")).getYear();
        }
        return year;
    }
}
